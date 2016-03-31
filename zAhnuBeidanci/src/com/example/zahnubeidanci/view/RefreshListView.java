package com.example.zahnubeidanci.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.zahnubeidanci.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class RefreshListView extends ListView {

		public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		addHeader();
		addFooter();
		initAnimation();
	}
		public RefreshListView(Context context) {
			super(context);
			addHeader();
			addFooter();
			initAnimation();
		}
		private int downY = -1;// 给downY 默认初始值，为了只赋值一次
		private int headerHeight;
		private View header;

		private static final int PULLDOWN_STATE = 0;// 下拉刷新状态
		private static final int RELEASE_STATE = 1;// 松开刷新状态
		private static final int REFRESHING_STATE = 2;// 正在刷新状态
		private int current_state = PULLDOWN_STATE;

		@ViewInject(R.id.iv_header_arrow)
		private ImageView iv_header_arrow;

		@ViewInject(R.id.pb_header_progress)
		private ProgressBar pb_header_progress;

		@ViewInject(R.id.tv_header_state)
		private TextView tv_header_state;

		@ViewInject(R.id.tv_header_time)
		private TextView tv_header_time;
		private RotateAnimation up;
		private RotateAnimation down;
		private MyOnRefreshListener mListener;
		private View footer;
		private int footerHeight;
		

		private void addFooter() {
			footer = View.inflate(getContext(), R.layout.refresh_footer, null);
			// 隐藏脚布局
			footer.measure(0, 0);
			footerHeight = footer.getMeasuredHeight();
			footer.setPadding(0, -footerHeight, 0, 0);
			addFooterView(footer);
			// 监听Listview滚动状态
			setOnScrollListener(new MyOnScrollListener());
		}

		private void initAnimation() {
			up = new RotateAnimation(0, -180, Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f);
			up.setDuration(500);
			up.setFillAfter(true);
			down = new RotateAnimation(-180, -360, Animation.RELATIVE_TO_SELF,
					0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			down.setDuration(500);
			down.setFillAfter(true);
		}

		private void addHeader() {
			header = View.inflate(getContext(), R.layout.refresh_header, null);
			ViewUtils.inject(this, header);
			// 隐藏头布局
			header.measure(0, 0);
			headerHeight = header.getMeasuredHeight();
			header.setPadding(0, -headerHeight, 0, 0);
			addHeaderView(header);
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			// System.out.println(ev.getAction());
			// System.out.println(getFirstVisiblePosition());
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				downY = (int) ev.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				if (downY == -1) {
					downY = (int) ev.getY();
				}
				int moveY = (int) ev.getY();
				if (getFirstVisiblePosition() != 0) {// 轮播图没有完全展示时，不处理下拉事件
					break;
				}
				if (current_state == REFRESHING_STATE) {
					break;
				}
				int diffY = moveY - downY;
				if (diffY > 0) {// 只处理下拉
					// padding = 手指移动的距离 - 头布局的高度
					int topPadding = diffY - headerHeight;
					// 根据topPadding值判断切换状态，topPadding==0 完全展示
					if (topPadding < 0 && current_state != PULLDOWN_STATE) {// 切换到下拉刷新状态
						current_state = PULLDOWN_STATE;
//						System.out.println("切换到下拉刷新状态");
						switchState(current_state);
					}
					// 切换到松开刷新状态
					else if (topPadding > 0 && current_state != RELEASE_STATE) {
						current_state = RELEASE_STATE;
//						System.out.println("切换到松开刷新状态");
						switchState(current_state);
					}

					header.setPadding(0, topPadding, 0, 0);
					return true;// 消费掉事件
				}
				break;
			case MotionEvent.ACTION_UP:
				downY = -1;
				// 根据当前状态，判断是否切换为正在刷新
				if (current_state == PULLDOWN_STATE) {
					// 把头布局隐藏
					header.setPadding(0, -headerHeight, 0, 0);
				} else if (current_state == RELEASE_STATE) {
					current_state = REFRESHING_STATE;
					// 把头布局正好完全展示
					header.setPadding(0, 0, 0, 0);
//					System.out.println("切换到正在刷新状态");
					switchState(current_state);
					// 调用外界的刷新业务
					if (mListener != null) {
						mListener.onRefreshing();
					}
				}
				break;

			default:
				break;
			}
			return super.onTouchEvent(ev);
		}

		private void switchState(int state) {
			switch (state) {
			case PULLDOWN_STATE:
				pb_header_progress.setVisibility(View.INVISIBLE);
				iv_header_arrow.setVisibility(View.VISIBLE);
				tv_header_state.setText("下拉刷新");
				iv_header_arrow.startAnimation(down);
				break;
			case RELEASE_STATE:
				iv_header_arrow.startAnimation(up);
				tv_header_state.setText("松开刷新");
				break;
			case REFRESHING_STATE:
				// 清除动画
				iv_header_arrow.clearAnimation();
				pb_header_progress.setVisibility(View.VISIBLE);
				iv_header_arrow.setVisibility(View.INVISIBLE);
				tv_header_state.setText("正在刷新");
				break;

			default:
				break;
			}
		}

		// 对外提供恢复状态的方法
		public void refreshFinished(boolean success) {
			current_state = PULLDOWN_STATE;
			header.setPadding(0, -headerHeight, 0, 0);
			pb_header_progress.setVisibility(View.INVISIBLE);
			iv_header_arrow.setVisibility(View.VISIBLE);
			tv_header_state.setText("下拉刷新");
			if (success) {
				SimpleDateFormat dateFormat = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String format = dateFormat.format(new Date());
				// 更新最后刷新时间
				tv_header_time.setText("最后刷新时间：" + format);
			} else {
				Toast.makeText(getContext(), "亲，网络出问题了", 0).show();
			}

		}

		// 对外暴露接口
		public interface MyOnRefreshListener {
			void onRefreshing();
			void onLoadingMore();
		}

		// 提供外界设置Listener的方法
		public void setMyOnRefreshListener(MyOnRefreshListener listener) {
			this.mListener = listener;
		}
		private boolean isLoadMore = false;// 当前是否处于加载更多
		
		// 恢复加载更多 的状态
		public void loadMoreFinished(){
			footer.setPadding(0, -footerHeight, 0, 0);
			isLoadMore = false;
		}
		public class MyOnScrollListener implements OnScrollListener {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 判断Listview滚动状态，停止状态或惯性停止时，且当前展示的最后一条是Adapter中的最后一条数据，显示加载更多布局
				if (OnScrollListener.SCROLL_STATE_IDLE == scrollState
						|| OnScrollListener.SCROLL_STATE_FLING == scrollState) {
					if(getLastVisiblePosition()==getCount()-1&&!isLoadMore){
						isLoadMore = true;
//						System.out.println("显示加载更多");
						footer.setPadding(0, 0, 0, 0);
						// 让加载更多脚布局自动显示
						setSelection(getCount());
						// 调用外界加载更多的业务
						if(mListener!=null){
							mListener.onLoadingMore();
						}
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}

		}
	}