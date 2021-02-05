package id.co.bni.ets.complain_online.service;

import id.co.bni.ets.complain_online.model.Card;
import id.co.bni.ets.complain_online.model.Complain;
import id.co.bni.ets.complain_online.model.Customer;
import id.co.bni.ets.complain_online.principal_repository.UserRepository;
import id.co.bni.ets.complain_online.repository.CardRepository;
import id.co.bni.ets.complain_online.repository.ComplainRepository;
import id.co.bni.ets.complain_online.repository.CustomerRepository;
import id.co.bni.ets.lib.model.entity.User;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Juang Nasution
 */
@Service
public class ComplainService {

    private static final String COMPLAIN_REPORT = "REPORT COMPLAIN";
    private static final String FORMAT_EXCEL = ".xlsx";
    private static final String NO_KOMPLAIN = "NOMOR KOMPLAIN";
    private static final String NASABAH_NAME = "NAMA NASABAH";
    private static final String NASABAH_EMAIL = "EMAIL NASABAH";
    private static final String COMPLAIN_DETAIL = "COMPLAIN DETAIL";
    private static final String COMPLAIN_RESPONSE = "COMPLAIN RESPONSE";
    private static final String DATE = "TANGGAL COMPLAIN";
    private static final String DONE_DATE = "TANGGAL RESPONSE";
    private static final String CARD_NUMBER = "NOMOR KARTU";
    private static final String[] HEADER = {NO_KOMPLAIN, NASABAH_NAME, NASABAH_EMAIL, COMPLAIN_DETAIL,
        COMPLAIN_RESPONSE, DATE, DONE_DATE, CARD_NUMBER};

    private final ComplainRepository complainRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    public ComplainService(ComplainRepository complainRepository, CustomerRepository customerRepository, CustomerService customerService, CardRepository cardRepository, UserRepository userRepository) {
        this.complainRepository = complainRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
    }

    public Page<Complain> getComplain(Long id, String searchTerm, Pageable pageable) {
        return complainRepository.findAllByCustomerId(findCustomerById(id), searchTerm,
                pageable);
    }

    public Page<Complain> getComplainAtm(String searchTerm, String category, Pageable pageable) {
        return complainRepository.findAllAtm(searchTerm, category,
                pageable);
    }

    public Complain getDetailComplain(Long id, String noComplain) {
        Complain complain = complainRepository.findById(noComplain).get();
        complain.setCardNumber(cardRepository.findById(complain.getCardId()).get().getCardNumber());
        return complain;
    }

    public List<Card> getCard(Long id, String searchTerm) {
        System.out.println(findCustomerById(id));
        System.out.println(searchTerm);
        return cardRepository.findByCustomerId(findCustomerById(id), searchTerm);
    }

    public int findCustomerById(Long id) {
        User user = userRepository.findById(id).get();
        return customerRepository.findByEmail(user.getUsername()).getId();
    }

    public String generateRandomString() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 96; // letter 'Z'
        int targetStringLength = 5;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return generatedString;
    }

    public void createComplain(Long id, Complain complain) {
        String noComplain = "BNI-" + generateRandomString() + "-" + new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        complain.setNoComplain(noComplain);
        complain.setStatus(0);
        complain.setCreatedDate(new Date());
        complain.setCustomerId(findCustomerById(id));
        complainRepository.save(complain);
    }

    public void responseComplain(String noComplain, String complainResponse) {
        Complain complain = complainRepository.findById(noComplain).get();
        Customer customer = customerRepository.findById(complain.getCustomerId()).get();
        complain.setComplainResponse(complainResponse);
        complain.setDoneDate(new Date());
        complain.setStatus(1);
//        customerService.sendMail(customer.getEmail());
        complainRepository.save(complain);
    }

    public String getFileName() {
        return COMPLAIN_REPORT + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date()) + FORMAT_EXCEL;
    }

    public byte[] getReport() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<Complain> complains = (List) complainRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        // Create a Sheet
        Sheet sheet = workbook.createSheet();
        //title properties
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Create Other rows and cells with employees data
        int rowNum = 1;

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workbook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create title row
        Row titleRow = sheet.createRow(0);
        int cellNum = 0;
        for (String string : HEADER) {
            Cell cell = titleRow.createCell(cellNum);
            cell.setCellValue(string);
            cell.setCellStyle(headerCellStyle);
            cellNum++;
        }

        for (Complain complain : complains) {
            Row row = sheet.createRow(rowNum++);
            cellNum = 0;
            Customer customer = customerRepository.findById(complain.getCustomerId()).get();
            for (String string : HEADER) {
                switch (string) {
                    case NO_KOMPLAIN:
                        row.createCell(cellNum)
                                .setCellValue(complain.getNoComplain());
                        cellNum++;
                        break;
                    case NASABAH_NAME:
                        row.createCell(cellNum)
                                .setCellValue(sdf.format(customer.getName()));
                        cellNum++;
                        break;
                    case NASABAH_EMAIL:
                        row.createCell(cellNum)
                                .setCellValue(customer.getEmail());
                        cellNum++;
                        break;
                    case COMPLAIN_DETAIL:
                        row.createCell(cellNum)
                                .setCellValue(sdf.format(complain.getComplainDetail()));
                        cellNum++;
                        break;
                    case COMPLAIN_RESPONSE:
                        row.createCell(cellNum)
                                .setCellValue(complain.getComplainDetail());
                        cellNum++;
                        break;
                    case DATE:
                        row.createCell(cellNum)
                                .setCellValue(complain.getCreatedDate());
                        cellNum++;
                        break;
                    case CARD_NUMBER:
                        row.createCell(cellNum)
                                .setCellValue(cardRepository.findById(complain.getCardId()).get().getCardNumber());
                        cellNum++;
                        break;
                    default:
                        break;
                }
            }
        }
        //resize cells : use for i because just need number of column not a value.
        for (int i = 0;
                i < HEADER.length;
                i++) {
            sheet.autoSizeColumn(i);
        }

        try {
            workbook.write(bos);
        } catch (IOException e) {
            try {
                workbook.close();
            } catch (IOException ex) {
                // Do nothing.
            }
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        } finally {
            try {
                workbook.close();
            } catch (IOException ex) {
                // Do nothing.
            }
        }

        return bos.toByteArray();
    }
}
