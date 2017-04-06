package app.memo.com.memoapp.Database;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import app.memo.com.memoapp.R;

/**
 * Created by Msi-Locale on 26/03/2017.
 */

public class CursorAdapterMemo extends RecyclerView.Adapter<CursorAdapterMemo.ViewHolderMemo> {

    Context mContext;
    Cursor mCursor;
    ClickItem mClickItem;

    public CursorAdapterMemo(Context context) {
        this.mContext = context;
    }

    public void setmClickItem(ClickItem mClickItem) {
        this.mClickItem = mClickItem;
    }

    @Override
    public CursorAdapterMemo.ViewHolderMemo onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout,parent,false);
        CursorAdapterMemo.ViewHolderMemo viewHolderMemo = new ViewHolderMemo(view);
        return viewHolderMemo;
    }

    @Override
    public void onBindViewHolder(CursorAdapterMemo.ViewHolderMemo holder, int position) {


        int id = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract._ID);
        int title = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_TITLE);
        int note = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_NOTETXT);
        int date = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_DATE);
        int color = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_COLOR);
        mCursor.moveToPosition(position);

        String titleNote = mCursor.getString(title);
        String noteTxt = mCursor.getString(note);
        String dateNote = mCursor.getString(date);
        int colorNote = mCursor.getInt(color);
        int posId = mCursor.getInt(id);

        holder.itemView.setTag(posId);
        holder.mTitleNote.setText(titleNote);
        holder.mNoteText.setText(noteTxt);
        holder.mDateBox.setText(dateNote);
        holder.mColorBox1.setBackgroundColor(colorNote);
        holder.mColorBox2.setBackgroundColor(colorNote);


    }

    @Override
    public int getItemCount() {
        if (mCursor == null){
            return 0;
        }
        return  mCursor.getCount();
    }

    public Cursor swapCursor(Cursor cursor) {
        if (mCursor == cursor) {
            return null;
        }
        Cursor tempCursor = mCursor;
        this.mCursor = cursor;

        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return tempCursor;
    }


    public class ViewHolderMemo extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTitleNote;
        TextView mNoteText;
        TextView mDateBox;
        ImageButton mColorBox1;
        TextView mColorBox2;


        public ViewHolderMemo(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleNote = (TextView)itemView.findViewById(R.id.noteTitle);
            mNoteText = (TextView)itemView.findViewById(R.id.noteText);
            mDateBox = (TextView)itemView.findViewById(R.id.timebox);
            mColorBox1 = (ImageButton) itemView.findViewById(R.id.colorBarMain);
            mColorBox2 = (TextView)itemView.findViewById(R.id.colorBarMain2);
            mNoteText.setMovementMethod(new ScrollingMovementMethod());
        }

        @Override
        public void onClick(View view) {
            if (mClickItem != null){
                mClickItem.OnclickItem(view,getAdapterPosition());
            }
        }
    }
}
