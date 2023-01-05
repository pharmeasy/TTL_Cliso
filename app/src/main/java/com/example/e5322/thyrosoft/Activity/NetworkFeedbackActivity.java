package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.e5322.thyrosoft.Controller.FeedBackPostQuestionsController;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.FeedbackPostQuestionsRequestModel;
import com.example.e5322.thyrosoft.Models.FeedbackQuestionsResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.example.e5322.thyrosoft.startscreen.SplashScreen;

import java.util.ArrayList;

public class NetworkFeedbackActivity extends AppCompatActivity {

    ArrayList<FeedbackQuestionsResponseModel.OutputDTO> feedback = new ArrayList<>();
    private int position = 0;
    TextView tv_questions, tv_count, tv_title, tv_desc, tv_note;
    RatingBar rb_rating;
    ImageView iv_banner;
    LinearLayout ll_rating, ll_next, ll_prev, ll_success;
    ArrayList<FeedbackPostQuestionsRequestModel.FeedbacksDTO> postFeedback;
    FeedbackPostQuestionsRequestModel.FeedbacksDTO feedbacksDTO;
    Button btn_submit;
    EditText edt_remarks;
    String user;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network_feedback);

        tv_count = findViewById(R.id.tv_count);
        tv_questions = findViewById(R.id.tv_questions);
        rb_rating = findViewById(R.id.rb_rating);
        iv_banner = findViewById(R.id.iv_banner);
        tv_title = findViewById(R.id.tv_title);
        tv_desc = findViewById(R.id.tv_desc);
        tv_note = findViewById(R.id.tv_note);
        ll_rating = findViewById(R.id.ll_rating);
        ll_next = findViewById(R.id.ll_next);
        ll_prev = findViewById(R.id.ll_prev);
        ll_success = findViewById(R.id.ll_success);
        postFeedback = new ArrayList<>();
        btn_submit = findViewById(R.id.submitcomment);
        edt_remarks = findViewById(R.id.query);

        feedback = getIntent().getExtras().getParcelableArrayList("Questions");

        prefs = getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");

        SetQuestions();
        InitListerner();

    }

    private void InitListerner() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_submit.getText().toString().equalsIgnoreCase("Okay")) {
                    Intent intent = new Intent(NetworkFeedbackActivity.this, SplashScreen.class);
                    startActivity(intent);
                    finish();
                } else {
                    String getFeedback = edt_remarks.getText().toString();
                    if (getFeedback.equals("")) {
                        GlobalClass.toastyError(NetworkFeedbackActivity.this, "Kindly enter remarks", false);
                    } else if (getFeedback.length() > 250) {
                        GlobalClass.toastyError(NetworkFeedbackActivity.this, "kindly enter remarks upto 250 character", false);
                    } else {
                        for (int i = 0; i < feedback.size(); i++) {
                            feedbacksDTO = new FeedbackPostQuestionsRequestModel.FeedbacksDTO();
                            if (feedback.size() - 1 != i) {
                                feedbacksDTO.setEntryBy(user);
                                feedbacksDTO.setRating(String.valueOf(feedback.get(i).getRating()));
                                feedbacksDTO.setQuestionId(Integer.valueOf(feedback.get(i).getId()));
                                feedbacksDTO.setRemark("");
                            } else {
                                feedbacksDTO.setEntryBy(user);
                                feedbacksDTO.setRating("");
                                feedbacksDTO.setQuestionId(Integer.valueOf(feedback.get(i).getId()));
                                feedbacksDTO.setRemark(getFeedback);
                            }
                            postFeedback.add(feedbacksDTO);
                        }
                        FeedbackPostQuestionsRequestModel feedbackPostQuestionsRequestModel = new FeedbackPostQuestionsRequestModel();
                        feedbackPostQuestionsRequestModel.setFeedbacks(postFeedback);

                        FeedBackPostQuestionsController feedBackPostQuestionsController = new FeedBackPostQuestionsController(NetworkFeedbackActivity.this);
                        feedBackPostQuestionsController.CallAPI(feedbackPostQuestionsRequestModel);
                    }
                }
            }
        });
        edt_remarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(NetworkFeedbackActivity.this, ToastFile.ent_feedback, Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        edt_remarks.setText(enteredString.substring(1));
                    } else {
                        edt_remarks.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private void SetQuestions() {

        try {
            tv_questions.setText(feedback.get(position).getQuestions());
            questionsCount(position);

            rb_rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    feedback.get(position).setRating((int) rating);
                    ll_next.setVisibility(View.VISIBLE);
                }
            });

            ll_next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position != feedback.size() - 2) {
                        position = position + 1;
                        tv_questions.setText(feedback.get(position).getQuestions());
                        rb_rating.setRating(feedback.get(position).getRating());
                        questionsCount(position);

                        if (feedback.get(position).getRating() == 0) {
                            ll_next.setVisibility(View.GONE);
                        }
                    } else if (position == feedback.size() - 2) {
                        position++;
                        showRemarks();
                    }
                    if (position != 0) {
                        ll_prev.setVisibility(View.VISIBLE);
                    } else {
                        ll_prev.setVisibility(View.GONE);
                    }
                }
            });

            ll_prev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position > 0) {
                        position = position - 1;
                        tv_questions.setText(feedback.get(position).getQuestions());
                        rb_rating.setRating(feedback.get(position).getRating());
                        questionsCount(position);
                        if (position == 0) {
                            ll_prev.setVisibility(View.GONE);
                        }
                        if (position < feedback.size()) {
                            ll_next.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void questionsCount(int position) {
        tv_count.setText(position + 1 + "/" + (feedback.size() - 1));
    }

    private void showRemarks() {
        iv_banner.setImageDrawable(getResources().getDrawable(R.drawable.ic_feedback_remarks));
        tv_title.setText("Remarks");
        tv_desc.setText("We would appreciate if you write down immediate 2 challenges in remarks affecting your business.");
        tv_note.setVisibility(View.GONE);
        ll_rating.setVisibility(View.GONE);
        btn_submit.setVisibility(View.VISIBLE);
        edt_remarks.setVisibility(View.VISIBLE);

    }

    public void getAPIResponse() {
        btn_submit.setText("Okay");
        ll_success.setVisibility(View.VISIBLE);
        tv_note.setVisibility(View.GONE);
        tv_title.setVisibility(View.GONE);
        tv_desc.setVisibility(View.GONE);
        iv_banner.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_tick));
        edt_remarks.setVisibility(View.GONE);
    }

    @Override
    public void onBackPressed() {
    }
}