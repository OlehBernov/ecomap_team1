package com.ecomap.ukraine.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecomap.ukraine.R;
import com.ecomap.ukraine.models.Problem;
import com.ecomap.ukraine.models.ProblemType;
import com.ecomap.ukraine.ui.activities.SearchActivity;

import java.util.List;

public class ProblemAdapter extends RecyclerView.Adapter<ProblemAdapter.ViewHolder> {

    private List<Problem> dataSet;
    private SearchActivity searchActivity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;
        private View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            textView = (TextView)view.findViewById(R.id.search_item_title);
            imageView = (ImageView)view.findViewById(R.id.search_item_type);
        }

        public void setProblem(final Problem problem, final SearchActivity searchActivity) {
            textView.setText(problem.getTitle());
            imageView.setImageResource(getIconRes(problem.getProblemType()));
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    searchActivity.showProblemInformation(problem);
                }
            });
        }

        private int getIconRes(ProblemType problemType) {
            int resId;
            switch (problemType) {
                case FOREST_DESTRUCTION:
                    resId = R.drawable.type1;
                    break;
                case RUBBISH_DUMP:
                    resId = R.drawable.type2;
                    break;
                case ILLEGAL_BUILDING:
                    resId = R.drawable.type3;
                    break;
                case WATER_POLLUTION:
                    resId = R.drawable.type4;
                    break;
                case THREAD_TO_BIODIVERSITY:
                    resId = R.drawable.type5;
                    break;
                case POACHING:
                    resId = R.drawable.type6;
                    break;
                default:
                    resId = R.drawable.type7;
                    break;
            }
            return resId;
        }

    }

    public ProblemAdapter(List<Problem> dataSet, SearchActivity searchActivity) {
        this.dataSet = dataSet;
        this.searchActivity = searchActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.search_element, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int index) {
        Problem problemPlate = dataSet.get(index);
        viewHolder.setProblem(problemPlate, searchActivity);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void renewList(List<Problem> newList) {
        applyAndAnimateRemovals(newList);
        applyAndAnimateAdditions(newList);
        applyAndAnimateMovedItems(newList);
    }

    private void applyAndAnimateRemovals(List<Problem> newModels) {
        for (int i = dataSet.size() - 1; i >= 0; i--) {
            final Problem model = dataSet.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<Problem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final Problem model = newModels.get(i);
            if (!dataSet.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<Problem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final Problem model = newModels.get(toPosition);
            final int fromPosition = dataSet.indexOf(model);
            if ((fromPosition >= 0) && (fromPosition != toPosition)) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public Problem removeItem(int position) {
        final Problem model = dataSet.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, Problem model) {
        dataSet.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        final Problem model = dataSet.remove(fromPosition);
        dataSet.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }
}
