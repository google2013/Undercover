package cn.centurywar.undercover.view;

import http.PublishHandler;

import java.util.List;
import cn.centurywar.undercover.BaseActivity;
import cn.centurywar.undercover.R;
import cn.centurywar.undercover.mail_list;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author chenzheng_java
 * @description 该类的部分实现模仿了SimpleAdapter
 */
public class MailAdapter extends BaseAdapter {
	private Button hasPress;
	private List<MailUser> publishs; 
	Context context;  
    private mail_list callBackActivity=null;
    
    /**
     * 初始化Myadapter
     * @param context 回调函数，写上就行
     * @param publishs 这个是返回的数据LIST
     * @param uid
     */
    public MailAdapter(Context context,List<MailUser> publishs,String uid){  
        this.publishs = publishs;  
        this.context = context;  
     
    }  
    
    public void setCallBack(mail_list view){
    	this.callBackActivity=view;
    }
    
	
    public final class ViewHolder {  
        public ImageView imageUser;
        public TextView txtName;  
        public TextView txtPunish;  
        public TextView txtTime;  
        public Button btnDel;
    }
	@Override
	public int getCount() {
		 return (publishs==null)?0:publishs.size();  
	}
	@Override
	public Object getItem(int position) {
		return publishs.get(position);  
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MailUser temPublish = (MailUser) getItem(position);
		final ViewHolder viewHolder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.mail_list_content, null);
			viewHolder = new ViewHolder();
			viewHolder.imageUser = (ImageView) convertView
					.findViewById(R.id.imgPhoto);
			viewHolder.txtName = (TextView) convertView
					.findViewById(R.id.txtName);
			viewHolder.txtTime = (TextView) convertView
					.findViewById(R.id.txtTime);
			viewHolder.txtPunish = (TextView) convertView
					.findViewById(R.id.txtPunish);
			viewHolder.btnDel = (Button) convertView
					.findViewById(R.id.btnDel);
			convertView.setTag(viewHolder);
			
			//设置各控件的内容在这里
			viewHolder.txtName.setText(temPublish.fromname);  
	        viewHolder.txtPunish.setText(temPublish.content);
	        viewHolder.txtTime.setText(temPublish.time);

	        if(temPublish.photo.length()>0){
	        	ImageFromUrl(viewHolder.imageUser,temPublish.photo,R.drawable.default_photo);
	        }
	        
	        
	        convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(hasPress!=null){
						hasPress.setVisibility(View.GONE);
					}
					viewHolder.btnDel.setVisibility(View.VISIBLE);
					hasPress=viewHolder.btnDel;
				}
			});
	        
	        final View temview=convertView;
	        viewHolder.btnDel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					callBackActivity.removeMail(temPublish.id);
					publishs.remove(position);
					notifyDataSetChanged();
//					temview.su.setVisibility(View.GONE);
				}
			});
	        
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		

        
      
		
		return convertView;
	}  

	public void ImageFromUrl(ImageView imageView,String url,int defaultphoto){
		//第一次调用初始化
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).build();
		ImageLoader.getInstance().init(config);
		DisplayImageOptions options;  
		options = new DisplayImageOptions.Builder()  
		 .showImageOnLoading(defaultphoto) //设置图片在下载期间显示的图片  
		 .showImageForEmptyUri(defaultphoto)//设置图片Uri为空或是错误的时候显示的图片  
		.build();//构建完成  
		ImageLoader.getInstance().displayImage(url, imageView,options);
	}
	
	
	/**
	 * @author wanhin
	 *真心话大冒险类，需要在里面添加like,dislike内容
	 */
	public static class MailUser {
		public int id;
		public String content;
		public String fromname;
		public String time;
		public String photo;
		public boolean isread;

		/**
		 * @param id
		 * @param name
		 * @param content
		 * @param photo
		 */
		public MailUser(int id, String content, String fromname,String photo,String time,boolean isread) {
			super();
			this.id = id;
			this.content = content;
			this.fromname=fromname;
			this.time=time;
			this.isread=isread;
			this.photo=photo;
		} 
		
	}  
}
