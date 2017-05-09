package app.memo.com.memoapp.Database;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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
    public void onBindViewHolder(final CursorAdapterMemo.ViewHolderMemo holder, int position) {

        int id = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract._ID);
        int title = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_TITLE);
        int note = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_NOTETXT);
        int date = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_DATE);
        int color = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_COLOR);
        int record = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_RECORD_AUDIO);
        int images = mCursor.getColumnIndexOrThrow(ContractMemoApp.MemoAppContract.COLUMN_IMAGES);

        mCursor.moveToPosition(position);

        String titleNote = mCursor.getString(title);
        final String noteTxt = mCursor.getString(note);
        String dateNote = mCursor.getString(date);
        int colorNote = mCursor.getInt(color);
        String uriRecord = mCursor.getString(record);
        String uriImages = mCursor.getString(images);


        int posId = mCursor.getInt(id);

        holder.itemView.setTag(posId);
        holder.mTitleNote.setText(titleNote);
        holder.mNoteText.setText(noteTxt);
        holder.mDateBox.setText(dateNote);
        holder.mColorBox1.setBackgroundColor(colorNote);
        holder.mColorBox2.setBackgroundColor(colorNote);

        if (uriImages != null) {
            holder.mImageViewAttachIcon.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(R.drawable.ic_attach_file_card).into(holder.mImageViewAttachIcon);
        } else {
            holder.mImageViewAttachIcon.setVisibility(View.INVISIBLE);
        }


        if (uriRecord != null) {
            holder.mImageViewRecordIcon.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(R.drawable.ic_keyboard_voice).into(holder.mImageViewRecordIcon);
        } else {
            holder.mImageViewRecordIcon.setVisibility(View.INVISIBLE);
        }


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


    public class ViewHolderMemo extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

        TextView mTitleNote;
        TextView mNoteText;
        TextView mDateBox;
        ImageButton mColorBox1;
        TextView mColorBox2;
        ImageView mImageViewAttachIcon;
        ImageView mImageViewRecordIcon;

        public ViewHolderMemo(View itemView) {
            super(itemView);
            mTitleNote = (TextView)itemView.findViewById(R.id.noteTitle);
            mNoteText = (TextView) itemView.findViewById(R.id.noteTextCard);
            mDateBox = (TextView)itemView.findViewById(R.id.timebox);
            mColorBox1 = (ImageButton) itemView.findViewById(R.id.colorBarMain);
            mColorBox2 = (TextView)itemView.findViewById(R.id.colorBarMain2);
            mImageViewAttachIcon = (ImageView) itemView.findViewById(R.id.imageview_cursor);
            mImageViewRecordIcon = (ImageView) itemView.findViewById(R.id.imageview_cursor_record);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickItem != null){
                mClickItem.OnclickItem(view,getAdapterPosition());
            }

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Options");
            contextMenu.add(0, R.id.shareNote, 0, "Share").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, mNoteText.getText().toString());
                    shareIntent.setType("text/plain");
                    mContext.startActivity(Intent.createChooser(shareIntent, "Share note with..."));
                    return false;
                }
            });
        }


    }
}
