package com.example.gaofeng.tulingdemo.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast

import com.example.gaofeng.tulingdemo.R
import com.example.gaofeng.tulingdemo.adapter.DishesAdapter
import com.example.gaofeng.tulingdemo.adapter.NewsAdapter
import com.example.gaofeng.tulingdemo.model.bean.DishBean
import com.example.gaofeng.tulingdemo.model.bean.NewsBean
import com.example.gaofeng.tulingdemo.model.bean.UrlBean
import com.example.gaofeng.tulingdemo.model.eventmsg.UrlMsg

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

import butterknife.BindView
import butterknife.ButterKnife

class NewsActivity : AppCompatActivity() {
    @BindView(R.id.news_recycle)
    internal var news_recycle: RecyclerView? = null

    private var adapter: NewsAdapter? = null
    private var adapter2: DishesAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        ButterKnife.bind(this)
        EventBus.getDefault().register(this)

    }


    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun getNewsBeanEvent(newsBean: NewsBean?) {
        if (null != newsBean) {

            adapter = NewsAdapter(this, newsBean.list)
            news_recycle!!.layoutManager = GridLayoutManager(this, 2)
            if (null != adapter2) {
                news_recycle!!.swapAdapter(adapter, true)
            } else {
                news_recycle!!.adapter = adapter
            }

            adapter!!.setOnItemClickListener { view, position ->
                val url = newsBean.list[position].detailurl
                EventBus.getDefault().postSticky(UrlMsg(url, "新闻详情", newsBean.list[position].icon))
                startActivity(Intent(this@NewsActivity, NewsWebViewActivity::class.java))
            }
            supportActionBar!!.title = "新闻列表"

            EventBus.getDefault().removeStickyEvent(newsBean)
        } else {
            Toast.makeText(this, "暂无新闻", Toast.LENGTH_SHORT).show()
        }


    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun getDishesEvent(dishBean: DishBean?) {
        if (null != dishBean) {

            adapter2 = DishesAdapter(this, dishBean.list)
            news_recycle!!.layoutManager = GridLayoutManager(this, 2)
            if (null != adapter) {
                news_recycle!!.swapAdapter(adapter2, true)
            } else {
                news_recycle!!.adapter = adapter2
            }
            adapter2!!.setOnItemClickListener { view, position ->
                val url = dishBean.list[position].detailurl
                EventBus.getDefault().postSticky(UrlMsg(url, "菜品详情", dishBean.list[position].icon))
                startActivity(Intent(this@NewsActivity, NewsWebViewActivity::class.java))
            }
            supportActionBar!!.title = "菜谱列表"
            EventBus.getDefault().removeStickyEvent(dishBean)
        } else {
            Toast.makeText(this, "暂无菜谱", Toast.LENGTH_SHORT).show()
        }
    }


}
