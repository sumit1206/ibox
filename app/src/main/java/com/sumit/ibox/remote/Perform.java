package com.sumit.ibox.remote;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sumit.ibox.common.Constant;
import com.sumit.ibox.common.Utils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Perform {

    public static void validateLogin(Context context, final VolleyCallback callback, final String username, final String password){
        String URL = Constant.URL_LOGIN;
        Utils.logPrint(context.getClass(),"Hitting: ", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1 || flagSuccess == 0)
                                callback.noDataFound();
                            else
                                callback.onSuccess(jsonObject);
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void searchStudentList(Context context, final VolleyCallback callback, final String institutionId, final String klass,
                                         final String division, final String session){
        String URL = Constant.URL_STUDENT_LIST_BY_CLASS_SECTION;
        Utils.logPrint(context.getClass(),"Hitting: ", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("school_id", institutionId);
                params.put("class", klass);
                params.put("section", division);
//                params.put("session", session);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void uploadAttendence(Context context, final VolleyCallback callback, final String institutionId, final String klass,
                                        final String division, final String session, final String teacherId, final String date, final String period, final String studentsPresent, final String studentsAbsent){
        String URL = Constant.URL_UPLOAD_ATTENDANCE;
        Utils.logPrint(context.getClass(),"Hitting: ", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                'timestamp','year','class_id','section_id','comma_seperated_roll_present','comma_seperated_roll_absent'
                Map<String, String> params = new HashMap<>();
//                params.put("institute_id", institutionId);
                params.put("class_id", klass);
                params.put("section_id", division);
                params.put("year", session);
//                params.put("teacher_id", teacherId);
                params.put("timestamp", date);
//                params.put("period", period);
                params.put("comma_seperated_roll_present", studentsPresent);
                params.put("comma_seperated_roll_absent", studentsAbsent);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void homeworkFetch(Context context, final VolleyCallback callback, final String klass, final String division) {
        String URL = Constant.URL_STUDENT_HOMEWORK_FETCH;
        Utils.logPrint(context.getClass(), "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_name", klass);
                params.put("section_name", division );
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void resultFetch(Context context, final VolleyCallback callback, final String studentId, final String session) {
        String URL = Constant.URL_STUDENT_RESULT_FETCH;
        Utils.logPrint(context.getClass(), "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", studentId);
//                params.put("session", session);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void resultDetailFetch(Context context, final VolleyCallback callback, final String studentId, final String session,
                                         final String examName) {
        String URL = Constant.URL_STUDENT_RESULT_DETAIL_FETCH;
        Utils.logPrint(context.getClass(), "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", studentId);
//                params.put("session", session);
                params.put("exam_name", examName);

                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void attendenceFetch(Context context, final VolleyCallback callback, final String studentId, final String session,
                                         final String year, final String month) {
        String URL = Constant.URL_STUDENT_ATTENDENCE_FETCH;
        Utils.logPrint(context.getClass(), "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //student_id, session, month, year
                params.put("student_id", studentId);
                params.put("session", session);
                params.put("month", month);
                params.put("year", year);

                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void syllabusFetch(Context context, final VolleyCallback callback, final String intitutionId, final String klass,
                                    final String section, final String session, final String type) {
        String URL = Constant.URL_STUDENT_SYLLABUS_FETCH;
        Utils.logPrint(context.getClass(), "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //class, section, type
                params.put("intitutionId", intitutionId);
                params.put("session", session);
                params.put("class", klass);
                params.put("section", section);
                params.put("type", type);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void feesFetch(Context context, final VolleyCallback callback, final String studentId) {
        String URL = Constant.URL_STUDENT_FEES_FETCH;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", studentId);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void paymentUpdate(Context context, final VolleyCallback callback, final String studentId, final String monthList,
                                     final String classId, final String txnId, final String amount, final String fine,
                                     final String discount) {
        String URL = Constant.URL_STUDENT_FEES_PAYMENT_UPDATE;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                'student_id','month_id_list','class_id','txn_id','total_discount','total_fine','total_amount'
                params.put("student_id", studentId);
                params.put("month_id_list", monthList);
                params.put("class_id", classId);
                params.put("txn_id", txnId);
                params.put("total_discount", discount);
                params.put("total_fine", fine);
                params.put("total_amount", amount);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchGallery(Context context, final VolleyCallback callback) {
        String URL = Constant.URL_GALLERY_FETCH;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("serial", "20");
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchFeesBreakup(Context context, final VolleyCallback callback, final String classId, final String monthId) {
        String URL = Constant.URL_FEES_BREAKUP_FETCH;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //class_id, month_id
                params.put("class_id", classId);
                params.put("month_id", monthId);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void faqFetch(Context context, final VolleyCallback callback) {
        String URL = Constant.URL_FAQ_FETCH;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("serial", "20");
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void uploadQuestion(Context context, final VolleyCallback callback, final String username, final String question) {
        String URL = Constant.URL_ASK_QUESTION;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //ques, user_id
                params.put("ques", question);
                params.put("user_id", username);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void updateNumber(Context context, final VolleyCallback callback, final String username, final String usertype, final String contactNo) {
        String URL = Constant.URL_CHANGE_NUMBER;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //username, type, mob
                params.put("username", username);
                params.put("type", usertype);
                params.put("mob", contactNo);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void uploadLeave(Context context, final VolleyCallback callback, final String username,
                                   final String startDate, final String endDate, final String description, final String title) {
        String URL = Constant.URL_TEACHER_LEAVE_APPLY;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //teacher_user_name, start_date, end_date, description, title
                params.put("teacher_user_name", username);
                params.put("start_date", startDate);
                params.put("end_date", endDate);
                params.put("description", description);
                params.put("title", title);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchTeacherLeaveState(Context context, final VolleyCallback callback, final String username) {
        String URL = Constant.URL_TEACHER_LEAVE_STATUS;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("teacher_uname", username);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchBookList(Context context, final VolleyCallback callback, final String klass) {
        String URL = Constant.URL_FETCH_LIBRARY_BOOK;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("class_name", klass);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void requestBook(Context context, final VolleyCallback callback, final String studentId, final String bookId) {
        String URL = Constant.URL_REQUEST_LIBRARY_BOOK;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //book_id, student_id, issue_start_date, issue_end_date
                params.put("book_id", bookId);
                params.put("student_id", studentId);
                params.put("issue_start_date", "");
                params.put("issue_end_date", "");
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchMyBookList(Context context, final VolleyCallback callback , final String studentId) {
        String URL = Constant.URL_FETCH_MY_LIBRARY_BOOK;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("student_id", studentId);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchNotification(Context context, final VolleyCallback callback,
                                         final int userType, final String studentId, final String teacherId) {
        String URL = Constant.URL_FETCH_NOTIFICATION;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //user_type, student_id, teacher_id
                params.put("user_type", userType+"");
                params.put("student_id", studentId);
                params.put("teacher_id", teacherId);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchChatList(Context context, final VolleyCallback callback, final String username, final int myType) {
        String URL = Constant.URL_FETCH_CHAT_LIST;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //type , uname
                params.put("uname", username);
                params.put("type", myType+"");
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchChat(Context context, final VolleyCallback callback, final String threadId) {
        String URL = Constant.URL_FETCH_CHAT;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //message_thread_code
                params.put("message_thread_code", threadId);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void replyMessage(Context context, final VolleyCallback callback, final String senderType, final String username, final String threadId, final String message, final String time) {
        String URL = Constant.URL_REPLY;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //type,uname,message_thread_code,message,time
                params.put("type", senderType);
                params.put("uname", username);
                params.put("message_thread_code", threadId);
                params.put("message", message);
                params.put("time", time);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void initiateChat(Context context, final VolleyCallback callback, final String senderType, final String senderUsername, final String receiverType,
                                    final String receiverUsername, final String message, final String time) {
        String URL = Constant.URL_INITIATE_CHAT;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //sender_type , sender_uname , rec_type , rec_uname , message
                params.put("sender_type", senderType);
                params.put("sender_uname", senderUsername);
                params.put("rec_type", receiverType);
                params.put("rec_uname", receiverUsername);
                params.put("message", message);
                params.put("time", time);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchTeachers(Context context, final VolleyCallback callback, final String className, final String sectionName) {
        String URL = Constant.URL_FETCH_TEACHER;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //class_name,section_name
                params.put("class_name", className);
                params.put("section_name", sectionName);
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }

    public static void fetchArticles(Context context, final VolleyCallback callback) {
        String URL = Constant.URL_FETCH_ARTICLES;
        Utils.logPrint(Perform.class, "Hitting", URL);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Utils.logPrint(this.getClass(), "response", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            int flagSuccess = jsonObject.getInt("success");
                            if(flagSuccess == 1)
                                callback.onSuccess(jsonObject);
                            else
                                callback.noDataFound();
                        } catch (JSONException e) {
                            callback.onCatch(e);
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        callback.onError(error);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                //
                params.put("serial", "20");
                Utils.logPrint(getClass(),"Sending: ", params.toString());
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Constant.SERVER_TIMEOUT,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue req = (RequestQueue) Volley.newRequestQueue(context);
        req.add(stringRequest);
    }
}
