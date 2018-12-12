package com.letion.core

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.letion.core.glide.Glide4Engine
import com.zhihu.matisse.Matisse
import com.zhihu.matisse.MimeType
import kotlinx.android.synthetic.main.activity_main.*
import me.leolin.shortcutbadger.ShortcutBadger
import org.json.JSONObject
import java.nio.charset.Charset


class MainActivity : AppCompatActivity(), MainView {

    private val CHOOSE = 1
    private lateinit var presenter: MainPresenter
    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //tvContent.text = printJson(parseJson(readJson()))
        Thread {
            while (true) {
                ShortcutBadger.applyCount(applicationContext, (Math.random() * 1000).toInt())
                Thread.sleep(2000)
            }
        }.start()

        presenter = MainPresenter(this)

        val array = arrayOfNulls<String>(10)
        for (i in 0 until 10) {
            array[i] = "这是第${i}个位置"
        }

        listView.adapter = ArrayAdapter<String>(baseContext, android.R.layout.simple_list_item_1, array)

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, i, _ ->
            run {
                when (i) {
                    1 -> presenter.getBook()
                    2 -> presenter.cancelBook()
                    3 -> presenter.getBook(true)
                    4 -> presenter.cancelBook(true)
                    5 -> presenter.downloadApk()
                    6 -> presenter.cancelApk()
                    7 -> toPhoto(9)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (CHOOSE === requestCode && resultCode === Activity.RESULT_OK) {
            val selectedList = Matisse.obtainResult(data)
            val paths = listOf<String>()
            for (i in 0 until selectedList.size) {
                paths.plus(selectedList[i].path)
                presenter.uploadImages(paths)
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun toPhoto(maxSelectable: Int) {
        Matisse.from(this@MainActivity)
            .choose(MimeType.allOf())
            .countable(true)
            .maxSelectable(maxSelectable)
            .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
            .thumbnailScale(0.85f)
            .imageEngine(Glide4Engine())
            .forResult(CHOOSE)
    }

    fun readJson(): String {
        val inputStream = assets.open("emoji.json")
        val length = inputStream.available()
        val byteArray = ByteArray(length)
        inputStream.read(byteArray)
        inputStream.close()
        return String(byteArray, Charset.defaultCharset())
    }

    fun parseJson(json: String): List<Emoji> {
        val list = ArrayList<Emoji>()
        val jsonArray = JSONObject(json).optJSONArray("list")
        if (jsonArray != null) {
            for (i in 0 until jsonArray.length()) {
                val jsonObject = jsonArray.optJSONObject(i)
                list.add(Emoji(jsonObject?.optString("name"), jsonObject.optInt("unicode")))
            }
        }
        return list
    }

    fun printJson(array: List<Emoji>): String {
        val builder = StringBuilder()
        array.forEach {
            builder.append(it.unicodeTemp)
        }
        return builder.toString()
    }

    data class Emoji(var name: String?, var unicode: Int) {
        val unicodeTemp: String?
            get() = String(Character.toChars(unicode))
    }

    override fun dialog(): Dialog? {
        if (dialog == null) {
            dialog = ProgressDialog(this)
            (dialog as ProgressDialog).setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
            (dialog as ProgressDialog).max = 100
            (dialog as ProgressDialog).isIndeterminate = false
        }
        return dialog
    }

    override fun context(): Context {
        return baseContext
    }

    override fun showProgress(progress: Int) {
        runOnUiThread {
            if (dialog is ProgressDialog) {
                (dialog as ProgressDialog).progress = progress
            }
        }
    }
}
