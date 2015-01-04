package cn.centurywar.undercover;

import http.PublishHandler;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.centurywar.undercover.view.GameAdapter;
import cn.centurywar.undercover.view.GameAdapter.GameContent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class game_content extends BaseActivity {
//	WebView urlPage;
	TextView txtTitle;
	TextView txtContent;
	LinearLayout imgLayout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_content);
		txtTitle=(TextView)this.findViewById(R.id.txtTitle);
		txtContent=(TextView)this.findViewById(R.id.txtContent);
		imgLayout=(LinearLayout)this.findViewById(R.id.imgLayout);
		
		final int gameid=getIntent().getIntExtra("gameid", 0);
//		txtTitle.setText(Html.fromHtml("<p>游戏规则：</p><ol><li>两人游戏，在喝酒时候进行。</li><li>两人对坐，伸出双手。握拳代表零，手掌代表五。</li><li>两人同时喊『十五十五**』（**可以是零、五、十、十五、二十）。</li><li>计算两人伸出手指总和，如果猜中，则对方输，如果同时都猜中，则继续。</li></ol><p>活动评价：</p><p>&nbsp; 划拳游戏，适合酒场时候进行。喊出来，特别有气氛。</p>"));
		getGameInfo(gameid);
		
		txtTitle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getGameInfo(gameid);
			}
		});
		setNetGameIsNew(gameid,false);
	}
	protected void getGameInfo(int gameid){
		txtTitle.setText("正在获取游戏数据");
		PublishHandler publishHandler = new PublishHandler(this);
		publishHandler.getGameOne(gameid);
		imgLayout.removeAllViews();
		imgLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void MessageCallBack(JSONObject jsonobj,String cmd) {
		super.MessageCallBack(jsonobj,cmd);
		if(cmd.equals(ConstantControl.GAME_ONE))
		{
			try{
				String temstr=jsonobj.getString("data");
				JSONObject content=new JSONObject(temstr);
				updateMessage(content);
			}catch(Exception e){
				e.printStackTrace();
			}
			System.out.print(jsonobj.toString());
		}
	}
	
	
	@Override
	public void MessageCallBackWrong(String cmd) {
		super.MessageCallBackWrong(cmd);
		if(cmd.equals(ConstantControl.GAME_ONE))
		{
			try{
				txtTitle.setText("数据异常[点击刷新]");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	ImageGetter imageGetter = new ImageGetter() {
		@Override
		public Drawable getDrawable(String source) {
			//把文章的配图抽取出来放到最上面
			imgLayout.setVisibility(View.VISIBLE);
			newImage(source);
			Drawable drawable = getResources().getDrawable(R.drawable.imghode);
		    drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
		    return drawable;
		}
	}; 
	protected void newImage(String source){
		ImageView mImageView = new ImageView(this); 
//		mImageView.setBackgroundColor(Color.GRAY);
		imgLayout.addView(mImageView);
		ImageFromUrl(mImageView,getImgUrlSmall(source),R.drawable.fang_pink_pressed);
	}
	
	protected void updateMessage(JSONObject content){
		try {
			txtTitle.setText(content.getString("title")+"[点击刷新]");
			txtContent.setText(Html.fromHtml(content.getString("content"),imageGetter,null));
		} catch (JSONException e) {
			txtTitle.setText("数据异常[点击刷新]");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
