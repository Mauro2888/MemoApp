package app.memo.com.memoapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import app.memo.com.memoapp.MemoUtils.MemoUtils;
import app.memo.com.memoapp.database.ClickItem;
import app.memo.com.memoapp.database.ContractMemoApp;

/**
 * Created by Msi-Locale on 26/03/2017.
 */

public class CursorAdapterMemo extends RecyclerView.Adapter<CursorAdapterMemo.ViewHolderMemo> {

    Context mContext;
    Cursor mCursor;
    ClickItem mClickItem;

    public void setmClickItem(ClickItem mClickItem) {
        this.mClickItem = mClickItem;
    }

    public CursorAdapterMemo(Context context) {
        this.mContext = context;
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
        mCursor.moveToPosition(position);

        String titleNote = mCursor.getString(title);
        String noteTxt = mCursor.getString(note);
        String dateNote = mCursor.getString(date);
        int posId = mCursor.getInt(id);


        holder.itemView.setTag(posId);
        holder.mTitleNote.setText(titleNote);
        holder.mNoteText.setText(noteTxt);
        holder.mDateBox.setText(dateNote);


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

        public ViewHolderMemo(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mTitleNote = (TextView)itemView.findViewById(R.id.noteTitle);
            mNoteText = (TextView)itemView.findViewById(R.id.noteText);
            mDateBox = (TextView)itemView.findViewById(R.id.timebox);
        }

        @Override
        public void onClick(View view) {
            if (mClickItem != null){
                mClickItem.OnclickItem(view,getAdapterPosition());
            }

        }
    }
}
