package id.co.bni.ets.complain_online.service;

import id.co.bni.ets.complain_online.model.Card;
import id.co.bni.ets.complain_online.model.Complain;
import id.co.bni.ets.complain_online.model.ComplainTwitter;
import id.co.bni.ets.complain_online.model.Customer;
import id.co.bni.ets.complain_online.model.ProcessedTwitter;
import id.co.bni.ets.complain_online.principal_repository.UserRepository;
import id.co.bni.ets.complain_online.repository.CardRepository;
import id.co.bni.ets.complain_online.repository.ComplainRepository;
import id.co.bni.ets.complain_online.repository.ComplainTwitterRepository;
import id.co.bni.ets.complain_online.repository.CustomerRepository;
import id.co.bni.ets.complain_online.repository.ProcessedTwitterRepository;
import id.co.bni.ets.lib.model.entity.User;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import twitter4j.TwitterException;

/**
 *
 * @author Juang Nasution
 */
@Service
public class ComplainService {

    private static final String COMPLAIN_REPORT = "REPORT COMPLAIN ";
    private static final String FORMAT_EXCEL = ".xlsx";

    private static final String NO_KOMPLAIN = "NOMOR KOMPLAIN";
    private static final String SUBJECT = "SUBJECT";
    private static final String USERNAME = "USERNAME";
    private static final String NASABAH_NAME = "NAMA NASABAH";
    private static final String NASABAH_EMAIL = "EMAIL NASABAH";
    private static final String COMPLAIN_DETAIL = "COMPLAIN DETAIL";
    private static final String COMPLAIN_RESPONSE = "COMPLAIN RESPONSE";
    private static final String DATE = "TANGGAL COMPLAIN";
    private static final String DONE_DATE = "TANGGAL RESPONSE";
    private static final String CARD_NUMBER = "NOMOR KARTU";
    private static final String STATUS = "STATUS";
    private static final String[] HEADER = {NO_KOMPLAIN, SUBJECT, NASABAH_NAME, NASABAH_EMAIL, COMPLAIN_DETAIL,
        COMPLAIN_RESPONSE, DATE, DONE_DATE, CARD_NUMBER, STATUS};

    private static final String[] HEADER_TWT = {NO_KOMPLAIN, SUBJECT, USERNAME, COMPLAIN_DETAIL,
        COMPLAIN_RESPONSE, DATE, DONE_DATE, STATUS};

    private final ComplainRepository complainRepository;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final ComplainTwitterRepository complainTwitterRepository;
    private final TwitterService twitterService;
    private final ProcessedTwitterRepository twitterRepository;

    public ComplainService(ComplainRepository complainRepository, CustomerRepository customerRepository,
            CustomerService customerService, CardRepository cardRepository, UserRepository userRepository,
            ComplainTwitterRepository complainTwitterRepository, TwitterService twitterService,
            ProcessedTwitterRepository twitterRepository) {
        this.complainRepository = complainRepository;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.cardRepository = cardRepository;
        this.userRepository = userRepository;
        this.complainTwitterRepository = complainTwitterRepository;
        this.twitterService = twitterService;
        this.twitterRepository = twitterRepository;
    }

    public Page<Complain> getComplain(Long id, String searchTerm, Pageable pageable) {
        return complainRepository.findAllByCustomerId(findCustomerById(id), searchTerm,
                pageable);
    }

    public Page<Complain> getComplainApp(String searchTerm, String category, Pageable pageable) {
        return complainRepository.findAllAtm(searchTerm, category,
                pageable);
    }

    public Page<Complain> getExportApp(Date fromDate, Date toDate, String category, Pageable pageable) {
        java.sql.Date fromDateSql = new java.sql.Date(fromDate.getTime());
        java.sql.Date toDateSql = new java.sql.Date(toDate.getTime());
        return complainRepository.findByLoadDateBetween(fromDateSql, toDateSql, category,
                pageable);
    }

    public Page<ComplainTwitter> getComplainTwt(String searchTerm, String category, Pageable pageable) {
        return complainTwitterRepository.findAllTwitter(searchTerm, category, pageable);
    }

    public Page<ComplainTwitter> getExportTwt(Date fromDate, Date toDate, String category, Pageable pageable) {
        java.sql.Date fromDateSql = new java.sql.Date(fromDate.getTime());
        java.sql.Date toDateSql = new java.sql.Date(toDate.getTime());
        return complainTwitterRepository.findByLoadDateBetween(fromDateSql, toDateSql, category,
                pageable);
    }

    public Complain getDetailComplainApp(Long id, String noComplain) {
        Complain complain = complainRepository.findById(noComplain).get();
        complain.setCardNumber(cardRepository.findById(complain.getCardId()).get().getCardNumber());
        return complain;
    }

    public ComplainTwitter getDetailComplainTwt(String noComplain) {
        ComplainTwitter complain = complainTwitterRepository.findById(noComplain).get();
        return complain;
    }

    public List<Card> getCard(Long id, String searchTerm) {
        System.out.println(findCustomerById(id));
        System.out.println(searchTerm);
        return cardRepository.findByCustomerId(findCustomerById(id), searchTerm);
    }

    public int findCustomerById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "id not found."));
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

    public void createComplainApp(Long id, Complain complain) {
        String noComplain = "BNI-" + generateRandomString() + "-" + new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        complain.setNoComplain(noComplain);
        complain.setStatus(0);
        complain.setCreatedDate(new Date());
        complain.setCustomerId(findCustomerById(id));
        complainRepository.save(complain);
    }

    public void createComplainTwt(ComplainTwitter complain) {
        String noComplain = "TWT-" + generateRandomString() + "-" + new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date());
        complain.setNoComplain(noComplain);
        complain.setStatus(0);
        complain.setCreatedDate(new Date());
        complainTwitterRepository.save(complain);
        dropTwt(complain.getIdStatus());
    }

    public void dropTwt(String id) {
        twitterRepository.save(new ProcessedTwitter(id));
    }

    public void responseComplainApp(String noComplain, Complain c) {
        Complain complain = complainRepository.findById(noComplain).get();
        Customer customer = customerRepository.findById(complain.getCustomerId()).get();
        complain.setComplainResponse(c.getComplainResponse());
        complain.setDoneDate(new Date());
        complain.setStatus(1);
//        customerService.sendMail(customer.getEmail());
        complainRepository.save(complain);
    }

    public void responseComplainTwt(String noComplain, ComplainTwitter c) {
        ComplainTwitter complain = complainTwitterRepository.findById(noComplain).get();
        complain.setComplainResponse(c.getComplainResponse());
        complain.setResponseDate(new Date());
        complain.setStatus(1);
        complainTwitterRepository.save(complain);
        try {
            twitterService.sendDirectMessage(complain);
        } catch (TwitterException ex) {
            Logger.getLogger(ComplainService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getFileName(String category) {
        return COMPLAIN_REPORT + " " + category + " " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new java.util.Date()) + FORMAT_EXCEL;
    }

    public byte[] getReport(Date fromDate, Date toDate, String category) {
        java.sql.Date fromDateSql = new java.sql.Date(fromDate.getTime());
        java.sql.Date toDateSql = new java.sql.Date(toDate.getTime());
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<Complain> complains = (List) complainRepository.downloadAtm(fromDateSql, toDateSql, category);
        XSSFWorkbook workbook = new XSSFWorkbook();
        // Create a Sheet
        XSSFSheet sheet = workbook.createSheet("Complain Application");
        //title properties
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 14);
        headerFont.setColor(IndexedColors.BLACK.getIndex());
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
                    case SUBJECT:
                        row.createCell(cellNum)
                                .setCellValue(complain.getSubject());
                        cellNum++;
                        break;
                    case NASABAH_NAME:
                        row.createCell(cellNum)
                                .setCellValue(customer.getName());
                        cellNum++;
                        break;
                    case NASABAH_EMAIL:
                        row.createCell(cellNum)
                                .setCellValue(customer.getEmail());
                        cellNum++;
                        break;
                    case COMPLAIN_DETAIL:
                        row.createCell(cellNum)
                                .setCellValue(complain.getComplainDetail());
                        cellNum++;
                        break;
                    case COMPLAIN_RESPONSE:
                        if (complain.getComplainDetail() != null) {
                            row.createCell(cellNum)
                                    .setCellValue(complain.getComplainDetail());
                        } else {
                            row.createCell(cellNum)
                                    .setCellValue("-");
                        }
                        cellNum++;
                        break;
                    case DATE:
                        row.createCell(cellNum)
                                .setCellValue(complain.getCreatedDate().toString());
                        cellNum++;
                        break;
                    case DONE_DATE:
                        if (complain.getDoneDate() != null) {
                            row.createCell(cellNum)
                                    .setCellValue(complain.getDoneDate().toString());
                        } else {
                            row.createCell(cellNum)
                                    .setCellValue("-");
                        }
                        cellNum++;
                        break;
                    case CARD_NUMBER:
                        row.createCell(cellNum)
                                .setCellValue(cardRepository.findById(complain.getCardId()).get().getCardNumber());
                        cellNum++;
                        break;
                    case STATUS:
                        row.createCell(cellNum)
                                .setCellValue(complain.getStatus() == 1 ? "Selesai" : "OnProgress");
                        cellNum++;
                        break;
                    default:
                        break;
                }
            }
        }
        XSSFSheet sheetTwt = workbook.createSheet("Complain Twitter");
        List<ComplainTwitter> complainTwitters = complainTwitterRepository
                .exportTwitter(fromDateSql, toDateSql, category);

        rowNum = 1;
        cellNum = 0;
        Row titleRow3 = sheetTwt.createRow(0);
        for (String string : HEADER_TWT) {
            Cell cell = titleRow3.createCell(cellNum);
            cell.setCellValue(string);
            cell.setCellStyle(headerCellStyle);
            cellNum++;
        }
        for (ComplainTwitter complainTwt : complainTwitters) {
            Row row = sheetTwt.createRow(rowNum++);
            cellNum = 0;
            for (String string : HEADER_TWT) {
                switch (string) {
                    case NO_KOMPLAIN:
                        row.createCell(cellNum)
                                .setCellValue(complainTwt.getNoComplain());
                        cellNum++;
                        break;
                    case SUBJECT:
                        row.createCell(cellNum)
                                .setCellValue(complainTwt.getSubject());
                        cellNum++;
                        break;
                    case USERNAME:
                        row.createCell(cellNum)
                                .setCellValue(complainTwt.getUsername());
                        cellNum++;
                        break;
                    case COMPLAIN_DETAIL:
                        row.createCell(cellNum)
                                .setCellValue(complainTwt.getComplainDetail());
                        cellNum++;
                        break;
                    case COMPLAIN_RESPONSE:
                        if (complainTwt.getComplainDetail() != null) {
                            row.createCell(cellNum)
                                    .setCellValue(complainTwt.getComplainDetail());
                        } else {
                            row.createCell(cellNum)
                                    .setCellValue("-");
                        }
                        cellNum++;
                        break;
                    case DATE:
                        row.createCell(cellNum)
                                .setCellValue(complainTwt.getCreatedDate().toString());
                        cellNum++;
                        break;
                    case DONE_DATE:
                        if (complainTwt.getResponseDate() != null) {
                            row.createCell(cellNum)
                                    .setCellValue(complainTwt.getResponseDate().toString());
                        } else {
                            row.createCell(cellNum)
                                    .setCellValue("-");
                        }
                        cellNum++;
                        break;
                    case STATUS:
                        row.createCell(cellNum)
                                .setCellValue(complainTwt.getStatus() == 1 ? "Selesai" : "OnProgress");
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

        for (int i = 0;
                i < HEADER_TWT.length;
                i++) {
            sheetTwt.autoSizeColumn(i);
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
