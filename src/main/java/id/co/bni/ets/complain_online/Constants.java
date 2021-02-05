package id.co.bni.ets.complain_online;

public class Constants {

    //file properties
    public static final String FILE_NAME = "fileName";
    public static final String SIZE = "size";
    public static final String FILE_CREATION_DATE = "fileCreationDate";
    public static final String UNSORTED = "UNSORTED";
    //ftp properties
    public static final String FTP_SERVER_NAME = "ftp_server_name";
    public static final String FTP_USERNAME = "ftp_username";
    public static final String FTP_PASSWORD = "ftp_password";
    public static final String FTP_PORT = "ftp_port";
    public static final String SOURCE_PAYROLL = "source_payroll";
    public static final String STORE_PAYROLL = "store_payroll";
    public static final String SOURCE_DMA = "source_dma";
    public static final String STORE_DMA = "store_dma";
    public static final String OUTPUT_DIR = "output_dir";
    public static final String STORE_CEMTEX = "store_cemtex";
    public static final String[] ARRAY_SETTING = {Constants.FTP_SERVER_NAME, Constants.FTP_USERNAME,
        Constants.FTP_PASSWORD, Constants.FTP_PORT, Constants.SOURCE_DMA, Constants.SOURCE_PAYROLL,
        Constants.STORE_DMA, Constants.STORE_PAYROLL, Constants.OUTPUT_DIR, Constants.STORE_CEMTEX};

    //scheduler properties
    public static final String LOAD_SCHEDULER = "load_scheduler";
    public static final String GENERATE_SCHEDULER = "generate_scheduler";
    public static final String PAYROLL_SCHEDULER = "payroll_scheduler";

    //status value
    public static final char STATUS_UNPROCESSED = '0';
    public static final char STATUS_CEMTEX_PROCESSED = '1';
    public static final char STATUS_PAYROLL_PROCESSED = '2';

    public static final String ALTO = "ALTO";
    public static final String BERSAMA = "BERSAMA";
    public static final String LINK = "LINK";
    public static final String MERAH_PUTIH = "MERAH PUTIH";
    public static final String PRIMA = "PRIMA";

    //payrollstat properties
    public static final String BRANCH = "branch";
    public static final String SEQUENCE_NUMBER = "sequenceNumber";
    public static final String FROM_ACCOUNT = "fromAccount";
    public static final String TO_ACCOUNT = "toAccount";
    public static final String AMOUNT = "amount";
    public static final String JOURNAL_NO = "journalNo";
    public static final String ERROR_CODE = "errorCode";
    public static final String ERROR_DESC = "errorDesc";
    public static final String DEBIT_ID = "debitId";

    //header file DMA
    public static final String HEAD_PAN = "PAN";
    public static final String HEAD_ARN = "ARN";
    public static final String HEAD_MCC = "MCC";
    public static final String HEAD_TRAN_DATE = "TRAN DATE";
    public static final String HEAD_APPR_CODE = "APPR CODE";
    public static final String HEAD_AMMOUNT_BILL = "AMOUNT BILL";
    public static final String HEAD_MERCHANT_NAME = "MERCHANT NAME";
    public static final String HEAD_ACC_FROM = "ACC_FROM";

    //cemtex properties
    public static final String FINANCIAL_DETAIL = "financial_detail";
    public static final String HEADER_DETAIL = "header_detail";
    public static final String NARRATIVE_DETAIL = "narrative_detail";
    public static final String FOOTER_DETAIL = "footer_detail";
    public static final String SUPPLIER_NAME = "supplier_name";
    public static final String SUPPLIER_NO = "supplier_no";
    public static final String USER_NAME = "user_name";
    public static final String USER_NO = "user_no";
    public static final String USER_REFERENCE = "user_reference";
    public static final String TRANSACTION_CODE = "transaction_code";
    public static final String INDICATOR = "indicator";
    public static final String GL_ACCOUNT = "gl_account";
    public static final String FROM_ACCOUNT_CEMTEX = "from_account";
    public static final String[] ARRAY_CEMTEX = {Constants.FINANCIAL_DETAIL, Constants.BRANCH, Constants.HEADER_DETAIL,
        Constants.NARRATIVE_DETAIL, Constants.FOOTER_DETAIL, Constants.SUPPLIER_NAME, Constants.SUPPLIER_NO,
        Constants.USER_NAME, Constants.USER_NO, Constants.USER_REFERENCE, Constants.TRANSACTION_CODE,
        Constants.INDICATOR, Constants.GL_ACCOUNT, Constants.FROM_ACCOUNT_CEMTEX};
}
