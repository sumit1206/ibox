package com.sumit.ibox.common;

import android.Manifest;

public class Constant {
    public static String[] permissionArray = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION};

    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    public static final int SERVER_TIMEOUT = 5000;
    public static final String APP_DATE_FORMAT = "dd MMM,yy";

    public static final String FILE_NAME_STUDENT_ROUTINE = "student_routine";
    public static final String FILE_NAME_TEACHER_ROUTINE = "teacher_routine";
    public static final String FILE_NAME_CLASS_SECTION = "class_section";
    public static final String FILE_NAME_HOLIDAY = "holidays";
    public static final String SAVE_PATH_CACHE_DUMP = "cache";
    public static final String DELIMETER = ",";
    public static final String LINE_SEPARATOR = "\n";
    public static final String NULL_VALUE = "-";

    public static final String ACTION_STUDENT_UPDATED = "action student updated";
    public static final String ACTION_TEACHER_UPDATED = "action teacher updated";

//    public static final String ROOT = "http://amra.dlinkddns.com/app_apis/";//"http://amra.ml/app_apis/";//https://sms-app.000webhostapp.com/"; //http://172.104.177.75/sms/
    public static final String ROOT = "http://ec2-13-233-50-76.ap-south-1.compute.amazonaws.com/app_apis/";
    public static final String URL_GENERATE_CHECKSUM = ROOT + "Paytm_Checksum/generateChecksum.php";
    public static final String URL_LOGIN = ROOT + "login_user_parent_teacher.php";
    public static final String URL_UPDATE_STUDENT_PROFILE = ROOT + "fetch_student_pofile.php";
    public static final String URL_UPDATE_TEACHER_PROFILE = ROOT + "fetch_teacher_profile.php";
    public static final String URL_HOMEWORK_UPLOAD = ROOT + "upload_homework.php";
    public static final String URL_STUDENT_LIST_BY_CLASS_SECTION = ROOT + "fetch_student_list_based_on_class_section.php";
    public static final String URL_STUDENT_HOMEWORK_FETCH = ROOT + "fetch_homework.php";
    public static final String URL_UPLOAD_ATTENDANCE = ROOT + "upload_student_attendance.php";
    public static final String URL_STUDENT_RESULT_FETCH = ROOT + "fetch_result.php";
    public static final String URL_STUDENT_RESULT_DETAIL_FETCH = ROOT + "result_brief_fetch.php";
    public static final String URL_STUDENT_ATTENDENCE_FETCH = ROOT + "fetch_attendance_based_on_student_id.php";
    public static final String URL_STUDENT_SYLLABUS_FETCH = ROOT + "fetch_syllabus.php";
    public static final String URL_SYLLABUS_UPLOAD = ROOT + "upload_syllabus.php";
    public static final String URL_FETCH_CLASS_SECTION = ROOT + "class_section_list_version_control.php";
    public static final String URL_STUDENT_FEES_FETCH = ROOT + "fetch_fees_details.php";
    public static final String URL_FETCH_STUDENT_ROUTINE = ROOT + "student_routine_fetch.php";
    public static final String URL_STUDENT_FEES_PAYMENT_UPDATE = ROOT + "after_payment_update.php";
    public static final String URL_GALLERY_FETCH = ROOT + "fetch_gallery.php";
    public static final String URL_FEES_BREAKUP_FETCH = ROOT + "fetch_fees_breakup.php";
    public static final String URL_FAQ_FETCH = ROOT + "fetch_faq.php";
    public static final String URL_ASK_QUESTION = ROOT + "ask_question.php";
    public static final String URL_CHANGE_NUMBER = ROOT + "change_phone_number.php";
    public static final String URL_FETCH_TEACHER_ROUTINE = ROOT + "teacher_routine_fetch.php";
    public static final String URL_TEACHER_LEAVE_APPLY = ROOT + "teacher_leave_apply.php";
    public static final String URL_TEACHER_LEAVE_STATUS = ROOT + "fetch_no_of_leave_tacken_by_teacher.php";
    public static final String URL_FETCH_HOLIDAY_LIST = ROOT + "fetch_holiday.php";
    public static final String URL_FETCH_LIBRARY_BOOK = ROOT + "library_book_fetch.php";
    public static final String URL_REQUEST_LIBRARY_BOOK = ROOT + "library_book_request.php";
    public static final String URL_FETCH_MY_LIBRARY_BOOK = ROOT + "library_issued_book_fetch.php";
    public static final String URL_FETCH_NOTIFICATION = ROOT + "fetch_notification.php";
    public static final String URL_FETCH_CHAT_LIST = ROOT + "fetch_message_threat.php";
    public static final String URL_FETCH_CHAT = ROOT + "fetch_message_based_on_threat_id.php";
    public static final String URL_REPLY = ROOT + "reply_message.php";
    public static final String URL_INITIATE_CHAT = ROOT + "send_sms.php";
    public static final String URL_FETCH_TEACHER = ROOT + "fetch_teacher_list_based_on_class_section.php";
    public static final String URL_FETCH_ARTICLES = ROOT + "articles_for_you.php";

    public static final String KEY_LOGGED_IN = "already logged in";
    public static final String KEY_USER_ID = "user id";
    public static final String KEY_USER_TYPE = "user type";
    public static final String KEY_INSTITUTION_ID = "institution id";
    public static final String KEY_TEACHER_ID = "use KEY_USER_ID instead";
    public static final String KEY_STUDENT_ID = "student id";
    public static final String KEY_SESSION = "session";
    public static final String KEY_STUDENT_CLASS = "student class";
    public static final String KEY_STUDENT_DIVISION = "student division";
    public static final String KEY_HOMEWORK_DATA = "homework data";
    public static final String KEY_RESULT_DATA = "result data";
    public static final String KEY_EXAM_NAME = "exam name result";
    public static final String KEY_ACTIVITY_TO_LOAD = "activity to load";
    public static final String KEY_CLASS_SECTION_VERSION = "class section version";
    public static final String KEY_STUDENT_ROUTINE_VERSION = "student routine version";
    public static final String KEY_TEACHER_ROUTINE_VERSION = "teacher routine version";
    public static final String KEY_FEES_AMOUNT = "amount";
    public static final String KEY_FEES_FINE = "fine";
    public static final String KEY_FEES_DISCOUNT = "discount";
    public static final String KEY_FEES_MONTH_LIST = "month list";
    public static final String KEY_FEES_MONTH_NAME_LIST = "month name list";
    public static final String KEY_FEES_CLASS_ID = "fees class id";
    public static final String KEY_EXTRA_CHAT_DATA = "key extra chat data";

    public static final int TYPE_PARENT = 2;
    public static final int TYPE_TEACHER = 3;
    public static final int TYPE_BOTH = 4;
    public static final int TYPE_ADMIN = 5;
    public static final int TYPE_OTHERS = 6;

    public static final int ATTENDENCE_PRESENT = 1;
    public static final int ATTENDENCE_ABSENT = 0;
    public static final int ATTENDENCE_WEEKEND = 2;
    public static final int ATTENDENCE_HOLIDAY = 3;

    public static final int ACADEMICS_HOMEWORK = 0;
    public static final int ACADEMICS_ASSIGNMENT = 1;
    public static final int ACADEMICS_SYLLABUS = 2;
    public static final int ACADEMICS_NOTES = 3;

    public static String[] classList = {"Class P.N","Class L.N","Class U.N","Class K.G","Class 1","Class 2",
            "Class 3","Class 4","Class 5","Class 6","Class 7","Class 8","Class 9","Class 10","Class 11","Class 12"};
    public static String[] sectionList = {"A","B","C","D","E"};
    public static final String[] periodList = {"All","1","2","3","4","5","6","7","8","9","10","11","12"};
    public static final String DUMMY = "dummy data";

    public static final int PERMISSION_REQUEST_CODE = 1000;
    public static final int CODE_GALLERY = 1001;
    public static final int CODE_CAMERA = 1002;
    public static final int CODE_FILES = 1003;
}
