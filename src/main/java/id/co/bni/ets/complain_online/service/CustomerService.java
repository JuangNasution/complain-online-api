package id.co.bni.ets.complain_online.service;

import id.co.bni.ets.complain_online.model.Card;
import id.co.bni.ets.complain_online.model.Customer;
import id.co.bni.ets.complain_online.model.CustomerCard;
import id.co.bni.ets.complain_online.repository.CardRepository;
import id.co.bni.ets.complain_online.repository.CustomerRepository;
import java.util.ArrayList;
import java.util.List;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Juang Nasution
 */
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CardRepository cardRepository;
    private final JavaMailSender javaMailSender;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder,
            CardRepository cardRepository, JavaMailSender javaMailSender) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
        this.cardRepository = cardRepository;
        this.javaMailSender = javaMailSender;
    }

    public boolean registCustomer(CustomerCard customerCard) {
        Customer customer = customerCard.getCustomer();
        if (customerRepository.findByEmail(customer.getEmail()) != null) {
            return false;
        }
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        customer.setIsActive('N');
        customerRepository.save(customer);
        List<Card> cards = new ArrayList<Card>();
        for (String string : customerCard.getCards()) {
            cards.add(new Card(string, customer.getId()));
        }
        cardRepository.saveAll(cards);
        return true;
    }

    public void sendMail(String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        //mailMessage.setFrom("bootcampbni@gmail.com");
        mailMessage.setTo(email);
        mailMessage.setSubject("Verification Email");
        mailMessage.setText("test verification");
        javaMailSender.send(mailMessage);
    }

    public String loginCustomer(String email, String password) {
        if (customerRepository.findByEmail(email) == null) {
            return "email not found";
        } else if (customerRepository.findByEmail(email).getIsActive().equals("N")) {
            return "account not activated yet";
        } else {
            Customer customer = customerRepository.findByEmail(email);
            String pass = customer.getPassword();
            if (!passwordEncoder.matches(pass, password)) {
                return "wrong password";
            }
        }
        return "login success";
    }
}
