package cn.kxlove.security.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import cn.kxlove.security.R;
import cn.kxlove.security.model.ContactInfo;

/**
 * @author: zhengkaixin
 * @date: 16/7/1
 */
public class ContactAdapter extends BaseAdapter {

    private List<ContactInfo> contactInfos;

    private Context context;

    public ContactAdapter(List<ContactInfo> contactInfos, Context context) {
        super();
        this.contactInfos = contactInfos;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_list_contact_select, null);
            holder = new ViewHolder();
            x.view().inject(holder,convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mNameTV.setText(contactInfos.get(position).name);
        holder.mPhoneTV.setText(contactInfos.get(position).phone);
        return convertView;
    }

    private static class ViewHolder{
        @ViewInject(R.id.tv_name)
        TextView mNameTV;
        @ViewInject(R.id.tv_phone)
        TextView mPhoneTV;
    }
}
