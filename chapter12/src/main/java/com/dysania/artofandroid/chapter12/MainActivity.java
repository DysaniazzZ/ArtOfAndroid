package com.dysania.artofandroid.chapter12;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.dysania.artofandroid.chapter12.util.ImageLoadUtil;
import com.dysania.artofandroid.chapter12.util.NetUtil;
import com.dysania.artofandroid.chapter12.util.UIUtil;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    private ImageView mImageView;

    private ImageLoadUtil mImageLoadUtil;

    private boolean mIsGridViewIdle = true;
    private boolean mIsWifi = false;
    private boolean mCanGetBitmapFromNetWork = false;

    private int mImageWidth = 0;

    private ImageAdapter mImageAdapter;
    private List<String> mUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mImageView = (ImageView) findViewById(R.id.image_view);
//        mImageView.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//        mImageView.setImageResource(R.drawable.bolt);
//        mImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.bolt));
//                mImageView.setImageBitmap(ImageResizeUtil
//                        .decodeBitmapFromResource(getResources(), R.drawable.bolt, mImageView.getWidth(), mImageView.getHeight()));
//            }
//        });

        initData();
        initView();
    }

    private void initData() {
        mImageLoadUtil = ImageLoadUtil.build(this);

        String[] imageUrls = {
                "http://b.hiphotos.baidu.com/zhidao/pic/item/a6efce1b9d16fdfafee0cfb5b68f8c5495ee7bd8.jpg",
                "http://pic47.nipic.com/20140830/7487939_180041822000_2.jpg",
                "http://pic41.nipic.com/20140518/4135003_102912523000_2.jpg",
                "http://img2.imgtn.bdimg.com/it/u=1133260524,1171054226&fm=21&gp=0.jpg",
                "http://h.hiphotos.baidu.com/image/pic/item/3b87e950352ac65c0f1f6e9efff2b21192138ac0.jpg",
                "http://pic42.nipic.com/20140618/9448607_210533564001_2.jpg",
                "http://pic10.nipic.com/20101027/3578782_201643041706_2.jpg",
                "http://img2.3lian.com/2014/c7/51/d/26.jpg",
                "http://img3.3lian.com/2013/c1/34/d/93.jpg",
                "http://b.zol-img.com.cn/desk/bizhi/image/3/960x600/1375841395686.jpg",
                "http://cdn.duitang.com/uploads/item/201311/03/20131103171224_rr2aL.jpeg",
                "http://imgrt.pconline.com.cn/images/upload/upc/tx/wallpaper/1210/17/c1/spcgroup/14468225_1350443478079_1680x1050.jpg",
                "http://pic41.nipic.com/20140518/4135003_102025858000_2.jpg",
                "http://www.1tong.com/uploads/wallpaper/landscapes/200-4-730x456.jpg",
                "http://pic.58pic.com/58pic/13/00/22/32M58PICV6U.jpg",
                "http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=429e7b1b92ef76c6d087f32fa826d1cc/7acb0a46f21fbe09cc206a2e69600c338744ad8a.jpg",
                "http://pica.nipic.com/2007-12-21/2007122115114908_2.jpg",
                "http://cdn.duitang.com/uploads/item/201405/13/20140513212305_XcKLG.jpeg",
                "http://photo.loveyd.com/uploads/allimg/080618/1110324.jpg",
                "http://img4.duitang.com/uploads/item/201404/17/20140417105820_GuEHe.thumb.700_0.jpeg",
                "http://cdn.duitang.com/uploads/item/201204/21/20120421155228_i52eX.thumb.600_0.jpeg",
                "http://img4.duitang.com/uploads/item/201404/17/20140417105856_LTayu.thumb.700_0.jpeg",
                "http://img04.tooopen.com/images/20130723/tooopen_20530699.jpg",
                "http://pic.dbw.cn/0/01/33/59/1335968_847719.jpg",
                "http://a.hiphotos.baidu.com/image/pic/item/a8773912b31bb051a862339c337adab44bede0c4.jpg",
                "http://h.hiphotos.baidu.com/image/pic/item/f11f3a292df5e0feeea8a30f5e6034a85edf720f.jpg",
                "http://img0.pconline.com.cn/pconline/bizi/desktop/1412/ER2.jpg",
                "http://pic.58pic.com/58pic/11/25/04/91v58PIC6Xy.jpg",
                "http://img3.3lian.com/2013/c2/32/d/101.jpg",
                "http://pic25.nipic.com/20121210/7447430_172514301000_2.jpg",
                "http://img02.tooopen.com/images/20140320/sy_57121781945.jpg",
                "http://pic129.nipic.com/file/20170516/20614752_221945581000_2.jpg",
                "http://pic130.nipic.com/file/20170515/20614752_224511276000_2.jpg"
        };

        for (String url : imageUrls) {
            mUrls.add(url);
        }

        int screenWidth = UIUtil.getScreenMetrics(this).widthPixels;
        int space = (int) UIUtil.dp2px(this, 20f);
        mImageWidth = (screenWidth - space) / 3;

        mIsWifi = NetUtil.isWifi(this);
        if (mIsWifi) {
            mCanGetBitmapFromNetWork = true;
        }
    }

    private void initView() {
        GridView gridView = (GridView) findViewById(R.id.grid_view);
        mImageAdapter = new ImageAdapter(this);
        gridView.setAdapter(mImageAdapter);

        gridView.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    mIsGridViewIdle = true;
                    mImageAdapter.notifyDataSetChanged();
                } else {
                    mIsGridViewIdle = false;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });

        if (!mIsWifi) {
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("注意")
                    .setMessage("初次使用时会从网络下载大约5MB的图片，确定要下载吗?")
                    .setPositiveButton("是", new OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mCanGetBitmapFromNetWork = true;
                            mImageAdapter.notifyDataSetChanged();
                        }
                    })
                    .setNegativeButton("否", null)
                    .show();
        }
    }

    private class ImageAdapter extends BaseAdapter {

        private LayoutInflater mLayoutInflater;

        public ImageAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mUrls.size();
        }

        @Override
        public String getItem(int position) {
            return mUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mUrls.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mLayoutInflater.inflate(R.layout.item_grid, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.mImageView = (ImageView) convertView.findViewById(R.id.image_view);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            ImageView imageView = viewHolder.mImageView;
            String tag = (String) imageView.getTag();
            String uri = getItem(position);

            if (!uri.equals(tag)) {
                imageView.setImageDrawable(new ColorDrawable(getResources().getColor(R.color.colorAccent)));
            }

            if (mIsGridViewIdle && mCanGetBitmapFromNetWork) {
                imageView.setTag(uri);
                mImageLoadUtil.bindBitmap(mUrls.get(position), imageView, mImageWidth, mImageWidth);
            }
            return convertView;
        }
    }

    private static class ViewHolder {
        public ImageView mImageView;
    }
}
