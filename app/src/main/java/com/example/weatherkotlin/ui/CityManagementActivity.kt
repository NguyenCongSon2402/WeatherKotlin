package com.example.weatherkotlin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.ui.adapter.CityManagementAdapter
import com.example.weatherkotlin.interfaces.RecyclerViewItemClickListener

class CityManagementActivity : AppCompatActivity(), RecyclerViewItemClickListener {

    private var isEditMode = false
    private lateinit var txt_SelectedItem: TextView
    private lateinit var txt_management: TextView
    private lateinit var btn_Back: ImageView
    private lateinit var btn_Cancel: ImageView
    private lateinit var btn_selectAll: ImageView
    private lateinit var btn_Delete: ImageView
    private lateinit var layout_delete: LinearLayout
    private lateinit var adapter: CityManagementAdapter

    private lateinit var recyclerView: RecyclerView
    private val selectedItems = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_management_layout)

        txt_SelectedItem = findViewById(R.id.txt_SelectedItem)
        txt_management = findViewById(R.id.txt_management)
        recyclerView = findViewById(R.id.RVCity)
        btn_Back = findViewById(R.id.btn_Back)
        btn_Cancel = findViewById(R.id.btn_Cancel)
        btn_selectAll = findViewById(R.id.btn_selectAll)
        btn_Delete = findViewById(R.id.btn_Delete)
        layout_delete = findViewById(R.id.layout_delete)
        val itemLongClickListener: RecyclerViewItemClickListener = this
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = layoutManager

        adapter = CityManagementAdapter(itemLongClickListener, txt_SelectedItem)
        recyclerView.adapter = adapter
        btn_Back.setOnClickListener {
            onBackPressed()
        }
// Xử lý sự kiện khi nhấn nút Cancel (chuyển về chế độ xem thông thường)
        btn_Cancel.setOnClickListener {
            // Ẩn các view đi
            btn_Back.visibility = View.VISIBLE
            txt_management.visibility = View.VISIBLE
            btn_Cancel.visibility = View.GONE
            btn_selectAll.visibility = View.GONE
            layout_delete.visibility = View.GONE
            layout_delete.visibility = View.GONE
            txt_SelectedItem.visibility = View.GONE

            isEditMode = false
            adapter.clearSelectedItems()
            // Cập nhật trạng thái của adapter
            adapter.setEditMode(false)
        }
        // Xử lý sự kiện khi nhấn nút Select All (chọn tất cả checkbox)
        btn_selectAll.setOnClickListener {
            if (adapter.getSelectedItems().size == adapter.itemCount) {
                // Đã chọn tất cả, bỏ chọn tất cả checkbox
                adapter.clearSelectedItems()
            } else {
                // Chưa chọn tất cả, chọn tất cả checkbox
                val allItems = mutableListOf<Int>()
                for (i in 0 until adapter.itemCount) {
                    allItems.add(i)
                }
                adapter.setSelectedItems(allItems)
            }
        }
        // Xử lý sự kiện khi nhấn nút Delete (xóa các item đã chọn)
        btn_Delete.setOnClickListener {
            adapter.deleteItemSelected()
            btn_Cancel.visibility = View.GONE
            btn_selectAll.visibility = View.GONE
            layout_delete.visibility = View.GONE
            txt_SelectedItem.visibility = View.GONE
            btn_Back.visibility = View.VISIBLE
            txt_management.visibility = View.VISIBLE
            isEditMode = false
        }
    }


    override fun onItemLongClick(position: Int) {
        // Xử lý hiển thị những thứ bị ẩn khi nhấn giữ lâu trên item ở vị trí position
        btn_Back.visibility = View.GONE
        txt_management.visibility = View.GONE
        btn_Cancel.visibility = View.VISIBLE
        btn_selectAll.visibility = View.VISIBLE
        layout_delete.visibility = View.VISIBLE
        txt_SelectedItem.visibility = View.VISIBLE

        // Đánh dấu adapter trong chế độ chỉnh sửa để hiển thị checkbox
        adapter.setEditMode(true)
    }

    fun showItemSelected(s: String) {
        txt_SelectedItem.text = s

    }
}