package app.memo.com.memoapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import app.memo.com.memoapp.database.ContractMemoApp;

/**
 * Created by Msi-Locale on 26/03/2017.
 */

public class CursorAdapterMemo extends RecyclerView.Adapter<CursorAdapterMemo.ViewHolderMemo> {

    Context mContext;
    Cursor mCursor;

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
        mCursor.moveToPosition(position);

        String titleNote = mCursor.getString(title);
        String noteTxt = mCursor.getString(note);
        int posId = mCursor.getInt(id);
        holder.itemView.setTag(posId);
        holder.mTitleNote.setText(titleNote);
        holder.mNoteText.setText(noteTxt);

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


    public class ViewHolderMemo extends RecyclerView.ViewHolder {

        TextView mTitleNote;
        TextView mNoteText;

        public ViewHolderMemo(View itemView) {
            super(itemView);

            mTitleNote = (TextView)itemView.findViewById(R.id.noteTitle);
            mNoteText = (TextView)itemView.findViewById(R.id.noteText);
        }
    }
}
