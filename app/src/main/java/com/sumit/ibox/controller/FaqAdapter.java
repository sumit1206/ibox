package com.sumit.ibox.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.sumit.ibox.R;
import com.sumit.ibox.model.FaqData;

import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.FaqViewHolder> {


    private Context faqContext;
    private List<FaqData> faqDataArrayList;

    public FaqAdapter(Context faqContext, List<FaqData> faqDataArrayList) {
        this.faqContext = faqContext;
        this.faqDataArrayList = faqDataArrayList;
    }

    @NonNull
    @Override
    public FaqViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_faq_layout, parent, false);
        return new FaqAdapter.FaqViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FaqViewHolder holder, int position) {
        FaqData faqData = faqDataArrayList.get(position);
        holder.faqCard.setAnimation(AnimationUtils.loadAnimation(faqContext, R.anim.fade_in));
        holder.faqQuestion.setText(faqData.getFaqQuestion());
        holder.faqAnswer.setText(faqData.getFaqAnswer());
        holder.faqConsumer.setText(faqData.getFaqConsumer());
//        holder.faqLikeCount.setText(faqData.getFaqLikeCount());
//        holder.faqUnlikeCount.setText(faqData.getFaqUnlikeCount());

//        holder.faq_unlike_btn.setImageDrawable(se);

//        holder.faq_unlike_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.faq_unlike_btn.setBackgroundColor(Color.parseColor("#FF8D00"));
//            }
//        });
//
//        holder.faq_like_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                holder.faq_like_btn.setBackgroundColor(Color.parseColor("#FF8D00"));
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return faqDataArrayList.size();
    }


    public class FaqViewHolder extends RecyclerView.ViewHolder {

        TextView faqQuestion,faqAnswer,faqConsumer,faqLikeCount,faqUnlikeCount;
        CardView faqCard;
        ImageView faq_like_btn,faq_unlike_btn;

        public FaqViewHolder(@NonNull View itemView) {
            super(itemView);

            faqQuestion = itemView.findViewById(R.id.faqQuestion);
            faqAnswer = itemView.findViewById(R.id.faqAnswer);
            faqConsumer = itemView.findViewById(R.id.faqConsumer);
//            faqLikeCount = itemView.findViewById(R.id.faqLikeCount);
//            faqUnlikeCount = itemView.findViewById(R.id.faqUnlikeCount);
            faqCard = itemView.findViewById(R.id.faq_card);
//            faq_like_btn = itemView.findViewById(R.id.faq_like_btn);
//            faq_unlike_btn = itemView.findViewById(R.id.faq_unlike_btn);
        }
    }
}
