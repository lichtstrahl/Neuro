package root.iv.neuro.ui.adapter;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import root.iv.neuro.R;
import root.iv.neuronet.Number;

public class NumberAdapter extends RecyclerView.Adapter<NumberAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private List<Number> numberList;
    private List<Bitmap> bitmapList;
    private View.OnClickListener listener;


    public NumberAdapter(LayoutInflater inflater, View.OnClickListener listener) {
        this.inflater = inflater;
        this.bitmapList = new LinkedList<>();
        this.numberList = new LinkedList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.item_number, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }

    public void append(Number number, Bitmap bitmap) {
        int count = numberList.size();
        numberList.add(number);
        bitmapList.add(bitmap);
        notifyItemInserted(count);
    }

    public void clear() {
        int count = numberList.size();
        numberList.clear();
        bitmapList.clear();
        notifyItemRangeRemoved(0, count);
    }

    public int getValue(int index) {
        return numberList.get(index).getValue();
    }

    public Number getItem(int index) {
        return numberList.get(index);
    }

    public List<Number> getNumbers() {
        return numberList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView preview;
        private TextView viewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            preview = itemView.findViewById(R.id.preview);
            viewName = itemView.findViewById(R.id.name);
            itemView.setOnClickListener(listener);
        }

        public void bind(int pos) {
            Bitmap bitmap = bitmapList.get(pos);
            Number number = numberList.get(pos);
            preview.setImageBitmap(bitmap);
            viewName.setText(String.valueOf(number.getValue()));
        }
    }
}
