package cn.kxlove.security.adapter;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import cn.kxlove.security.R;
import cn.kxlove.security.dao.BlackNumberDao;
import cn.kxlove.security.model.BlackContactInfo;

public class BlackContactAdapter extends BaseAdapter {

    private List<BlackContactInfo> contactInfos;
    private Context context;
    private BlackNumberDao dao;
    private BlackConactCallBack callBack;

    public void setCallBack(BlackConactCallBack callBack) {
        this.callBack = callBack;
    }

    public BlackContactAdapter(List<BlackContactInfo> systemContacts,
                               Context context) {
        super();
        this.contactInfos = systemContacts;
        this.context = context;
        dao = new BlackNumberDao();
    }

    @Override
    public int getCount() {
        return contactInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return contactInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context,
                    R.layout.item_list_blackcontact, null);
            holder = new ViewHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(position).contactName + "("
                + contactInfos.get(position).phoneNumber + ")");
        holder.mModeTV.setText(contactInfos.get(position).getModeString(
                contactInfos.get(position).mode));
        holder.mNameTV.setTextColor(context.getResources().getColor(
                R.color.bright_purple));
        holder.mModeTV.setTextColor(context.getResources().getColor(
                R.color.bright_purple));
        holder.mContactImgv
                .setBackgroundResource(R.drawable.brightpurple_contact_icon);
        holder.mDeleteView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean datele = dao.detele(contactInfos.get(position));
                if (datele) {
                    contactInfos.remove(contactInfos.get(position));
                    BlackContactAdapter.this.notifyDataSetChanged();
                    // 如果数据库中没有数据了，则执行回调函数
                    if (dao.getTotalNumber() == 0) {
                        callBack.DataSizeChanged();
                    }
                } else{
                    Toast.makeText(context, "删除失败！", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        @ViewInject(R.id.tv_black_name)
        TextView mNameTV;
        @ViewInject(R.id.tv_black_mode)
        TextView mModeTV;
        @ViewInject(R.id.view_black_icon)
        View mContactImgv;
        @ViewInject(R.id.view_black_delete)
        View mDeleteView;
    }

    public interface BlackConactCallBack {
        void DataSizeChanged();
    }
}
