package com.example.e5322.thyrosoft.Models.PincodeMOdel;

import android.app.Activity;
import android.content.Context;
import android.widget.TextView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.AppConstants;
import com.example.e5322.thyrosoft.Models.tspspinnerResponseModel;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;


/**
 * Created by Orion on 7/4/2017.
 */
public class ResponseParser implements AppConstants {


    private Activity activity;

    private Context context;

    private Gson gson;

    private AlertDialogMessage alertDialogMessage;

    private AppPreferenceManager appPreferenceManager;

    private TextView txtErrorMsg;

    private boolean isToSwitchActivity = true;

    private MessageModel messageModel;


    private boolean isToShowErrorDailog = true;

    private boolean isToShowToast = true;

    private static final int SPACE_COUNT_FOR_TOAST_TIME = 10;

    public void setToShowErrorDailog(boolean isToShowErrorDailog) {
        this.isToShowErrorDailog = isToShowErrorDailog;
    }

    public void setToShowToast(boolean isToShowToast) {
        this.isToShowToast = isToShowToast;
    }

    public ResponseParser(Activity activity) {

        this.activity = activity;
        if (activity.getApplicationContext() != null) {
            this.context = activity.getApplicationContext();
        }
        gson = new Gson();

        alertDialogMessage = new AlertDialogMessage();

        appPreferenceManager = new AppPreferenceManager(activity);

    }

    public ResponseParser(Context context) {

        this.context = context;

        gson = new Gson();

        alertDialogMessage = new AlertDialogMessage();

        appPreferenceManager = new AppPreferenceManager(context);

    }


    public int countSpacesInMessage(String message) {
        int counter = 0;
        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == ' ') {
                counter++;
            }
        }
        return counter;
    }


    public boolean parseIntoError(String json, int statusCode) {

        try {

            switch (statusCode) {
                case 401:
                    ErrorResponseModel busiError = gson.fromJson(json, ErrorResponseModel.class);
                    if (busiError != null && busiError.getError_description() != null) {
                        return parseAfterError(json, busiError);
                    } else {
                        SessionExpireModel sessionExpireModel = gson.fromJson(json, SessionExpireModel.class);
                        if (sessionExpireModel != null) {
                            if (activity != null) {
                                return parseAfterError(json, sessionExpireModel, activity, false);
                            } else {
                                return parseAfterError(json, sessionExpireModel, context, false);
                            }
                        }
                    }

                    break;
                case 400:
                    ErrorResponseModel businessErrorModel = gson.fromJson(json, ErrorResponseModel.class);
                    if (businessErrorModel != null && businessErrorModel.getError_description() != null) {
                        return parseAfterError(json, businessErrorModel);
                    }

                    ErrorResponseModel errorResponseModel = gson.fromJson(json, ErrorResponseModel.class);
                    if (errorResponseModel != null && errorResponseModel.getError_description() != null) {
                        if (isToShowToast && !DeviceUtils.isAppIsInBackground(context)) {
                            if (errorResponseModel.getError_description().contains(" ") && countSpacesInMessage(errorResponseModel.getError_description()) >= SPACE_COUNT_FOR_TOAST_TIME) {
                                GlobalClass.showTastyToast((Activity) context, errorResponseModel.getError_description(), 2);

                            } else {
                                GlobalClass.showTastyToast((Activity) context, errorResponseModel.getError_description(), 2);

                            }
                        }
                        return true;
                    }

                    MessageModel messageModel = null;
                    try {
                        messageModel = gson.fromJson(json, MessageModel.class);
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        return false;

                    }
                    if (messageModel != null
                            && messageModel.getMessages() != null
                            && (messageModel.getMessages().length > 0)) {

                        String message = "";

                        if (messageModel.getMessages() != null) {

                            for (MessageModel.FieldError msg : messageModel.getMessages()) {

                                if (msg != null) {
                                    message = message + msg.getMessage() + ",";
                                }

                            }

                        }

                        MessageModel.FieldError msg[] = messageModel.getMessages();

                        MessageModel.FieldError fieldError = msg[0];


                        //if (msg != null && msg.length>1) {
                        if (msg != null && isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)) {
                            message = msg[0].getMessage();

                            AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                            if (activity != null) {
                                alertDialogMessage1.showAlert(activity, message.toString(), true);
                            } else {
                                alertDialogMessage1.showAlert(context.getApplicationContext(), message.toString(), true);
                            }
                            //alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());


                        }


                        if (message.lastIndexOf(',') != -1 && isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)) {

                            message = message.substring(0, message.lastIndexOf(','));
                            AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();

                            if (activity != null) {
                                alertDialogMessage1.showAlert(activity, message.toString(), true);
                            } else {
                                alertDialogMessage1.showAlert(context.getApplicationContext(), message.toString(), true);
                            }
                            alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());

                        }
                    }

                    break;

                case 200:
                    ErrorModel errorModel = gson.fromJson(json, ErrorModel.class);
                    if (errorModel != null && errorModel.getError_description() != null && errorModel.getError() != null && (errorModel.getError().equalsIgnoreCase(ERROR_MESSAGE)
                            || errorModel.getError().equals(ERROR_MESSAGE_OPTIONAL))) {
                        parseAfterError(json, errorModel);
                    } else {
                        errorModel = null;
                        return false;
                    }

                case 500:

                    BusinessErrorModel businessErrorModel1 = gson.fromJson(json, BusinessErrorModel.class);
                    if (businessErrorModel1 != null) {
                        return parseInternalServerError(json, businessErrorModel1);
                    }
                    break;

                case 404:

                    BusinessErrorModel businessErrorModel2 = gson.fromJson(json, BusinessErrorModel.class);
                    if (businessErrorModel2 != null) {
                        return parseInternalServerError(json, businessErrorModel2);
                    }
                    break;
                default:
                    ErrorModel defaultErrorModel = gson.fromJson(json, ErrorModel.class);
                    if (defaultErrorModel != null && defaultErrorModel.getError_description() != null) {
                        if (defaultErrorModel.getError_description().equalsIgnoreCase("User logged out successfully")) {
                            //parseLogoutApi(json);
                            return true;
                        } else {

                            return parseAfterError(json, defaultErrorModel);
                        }
                    } else {
                        return parseAfterError(json, defaultErrorModel);
                    }
            }

        } catch (JsonSyntaxException js) {

            return true;

        }
        return true;

    }


    private boolean parseAfterError(String json, ErrorModel errorModel) {
        if (errorModel != null && errorModel.getError() != null && errorModel.getError_description() != null) {
            if (errorModel.getError_description().equals(MSG_SERVER_EXCEPTION)) {

                if (errorModel.getError_description().equals(MSG_INTERNET_CONNECTION_SLOW)) {

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getError_description().equals(MSG_COMMUNICATION_PROBLEM)) {

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getError_description().equals(MSG_NETWORK_ERROR)) {

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getError_description().equals(MSG_UNKNOW_ERROR)) {

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getError_description().equals("Please check your network connection.")) {

                    onApiFailed(errorModel);
                    return false;
                } else if (errorModel.getError().equalsIgnoreCase("ERR-BUSS")) {
                    onApiFailed(errorModel);
                } else if (errorModel.getError().equals(ERROR_MESSAGE) || errorModel.getError().equals(ERROR_MESSAGE_OPTIONAL) || (errorModel.getError_description() != null)) {
                    if (!errorModel.getError().equals(SUCCESS_MESSAGE) || !errorModel.getError().equals(SUCCESS_MESSAGE_OPTIONAL)) {
                        onApiFailed(errorModel);
                        return false;
                    } else {
                        return false;
                    }
                }
            } else {

                return true;
            }

        }

        return true;

    }

    public WithoutexpiryResponsemodel getWithoutexpiryResponsemodel(String json, int statusCode) {

        WithoutexpiryResponsemodel withoutexpiryResponsemodel = null;
        if (!parseIntoError(json, statusCode)) {
            withoutexpiryResponsemodel = gson.fromJson(json, WithoutexpiryResponsemodel.class);
        }
        return withoutexpiryResponsemodel;
    }

    public tspspinnerResponseModel gettspspinnerResponseModel(String json, int statusCode) {

        tspspinnerResponseModel tspspinnerResponseModel = null;
        if (!parseIntoError(json, statusCode)) {
            tspspinnerResponseModel = gson.fromJson(json, tspspinnerResponseModel.class);
        }
        return tspspinnerResponseModel;
    }

    public NedResponseModel getNedResponseModel(String json, int statusCode) {

        NedResponseModel nedResponseModel = null;
        if (!parseIntoError(json, statusCode)) {
            nedResponseModel = gson.fromJson(json, NedResponseModel.class);
        }
        return nedResponseModel;
    }

    public PgcsResponseModel getPgcsResponseModel(String json, int statusCode) {

        PgcsResponseModel pgcsResponseModel = null;
        if (!parseIntoError(json, statusCode)) {
            pgcsResponseModel = gson.fromJson(json, PgcsResponseModel.class);
        }
        return pgcsResponseModel;
    }

    public StaffResponseModel getStaffResponseModel(String json, int statusCode) {

        StaffResponseModel staffResponseModel = null;
        if (!parseIntoError(json, statusCode)) {
            staffResponseModel = gson.fromJson(json, StaffResponseModel.class);
        }
        return staffResponseModel;
    }

    public sgcsResponseModel getsgcsResponseModel(String json, int statusCode) {

        sgcsResponseModel sgcsResponseModel = null;
        if (!parseIntoError(json, statusCode)) {
            sgcsResponseModel = gson.fromJson(json, sgcsResponseModel.class);
        }
        return sgcsResponseModel;
    }

    public uploadspinnerResponseModel getuploadspinnerResponseModel(String json, int statusCode) {

        uploadspinnerResponseModel uploadspinnerResponseModel = null;
        if (!parseIntoError(json, statusCode)) {
            uploadspinnerResponseModel = gson.fromJson(json, uploadspinnerResponseModel.class);
        }
        return uploadspinnerResponseModel;
    }


    private boolean parseAfterError(String json, ErrorResponseModel businessErrorModel) {
        if (businessErrorModel != null && businessErrorModel.getError() != null && businessErrorModel.getError_description() != null) {

            if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)) {
                if (activity != null) {
                    alertDialogMessage.showAlert(activity, businessErrorModel.getError_description(), true);
                } else {
                    alertDialogMessage.showAlert(context.getApplicationContext(), businessErrorModel.getError_description(), true);
                }

                alertDialogMessage.setAlertDialogOkListener(new DialogOkButtonListener());
            }

            return false;

        }
        return true;
    }

    private boolean parseAfterError(String json, SessionExpireModel sessionExpireModel, Activity activity, boolean isDialogCancelable) {
        if (sessionExpireModel != null && sessionExpireModel.getMessage() != null) {
            if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)) {
                AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                alertDialogMessage1.showAlert(activity, sessionExpireModel.getMessage().toString(), isDialogCancelable);
                alertDialogMessage1.setAlertDialogOkListener(new InvalidSessionAlertDialogOkButtonListener());
            }
            return false;
        }
        return true;
    }

    private boolean parseAfterError(String json, SessionExpireModel sessionExpireModel, Context context, boolean isDialogCancelable) {
        if (sessionExpireModel != null && sessionExpireModel.getMessage() != null) {
            if (isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)) {
                AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                alertDialogMessage1.showAlert(context, sessionExpireModel.getMessage().toString(), true);
                alertDialogMessage1.setAlertDialogOkListener(new InvalidSessionAlertDialogOkButtonListener());
            }
            return false;
        }
        return true;
    }

    private boolean parseInternalServerError(String json, BusinessErrorModel businessErrorModel) {
        if (businessErrorModel != null && businessErrorModel.getType() != null && businessErrorModel.getMessages() != null
                && isToShowErrorDailog && !DeviceUtils.isAppIsInBackground(context)) {
            // onApiFailed(businessErrorModel);
            AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
            alertDialogMessage1.showAlert(activity, businessErrorModel.getMessages().getMessage().toString(), true);
            //alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());
            return false;
        }
        return true;
    }


    private class DialogOkButtonListener implements AlertDialogMessage.AlertDialogOkListener {

        @Override
        public void onAlertDialogOkButtonListener() {

        }

    }

    public void onApiFailed(ErrorModel errorModel) {

        if (errorModel != null) {

            if (activity != null && context != null && !DeviceUtils.isAppIsInBackground(context)) {
                AlertDialogMessage alertDialogMessage1 = new AlertDialogMessage();
                alertDialogMessage1.showAlert(activity, errorModel.getError_description().toString(), true);
                // alertDialogMessage1.setAlertDialogOkListener(new DialogOkButtonListener());

            }

        }


    }

    private class InvalidSessionAlertDialogOkButtonListener implements
            AlertDialogMessage.AlertDialogOkListener {

        @Override
        public void onAlertDialogOkButtonListener() {

            AppPreferenceManager appPreferenceManager = new AppPreferenceManager(
                    context);

			/*appPreferenceManager.clearAllPreferences();
            DhbDao dhbDao = new DhbDao(activity);
			dhbDao.deleteDb();


			Intent intent = new Intent(context, LoginActivity.class );
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);*/

        }

    }


    public DefaultuploadResponseModel getDefaultuploadResponseModel(String json, int statusCode) {

        DefaultuploadResponseModel defaultuploadResponseModel = null;
        if (!parseIntoError(json, statusCode)) {
            defaultuploadResponseModel = gson.fromJson(json, DefaultuploadResponseModel.class);
        }
        return defaultuploadResponseModel;
    }

}