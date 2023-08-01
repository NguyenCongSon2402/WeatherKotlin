package com.example.weatherkotlin.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.ui.adapter.CityManagementAdapter
import com.example.weatherkotlin.ui.interfaces.RecyclerViewItemClickListener

class CityManagementActivity : AppCompatActivity(), RecyclerViewItemClickListener {

    private var isEditMode = false
    private lateinit var txt_SelectedItem: TextView
    private lateinit var txt_management: TextView
    private lateinit var editTextSearch: AutoCompleteTextView
    private lateinit var btn_Back: ImageView
    private lateinit var btn_Cancel: ImageView
    private lateinit var btn_selectAll: ImageView
    private lateinit var btn_Delete: ImageView
    private lateinit var layout_delete: LinearLayout
    private lateinit var adapter: CityManagementAdapter
    private lateinit var autoCompleteAdapter: ArrayAdapter<String>
    private val citySuggestions = mutableListOf<String>()
    private val COUNTRIES = arrayOf(
        "Belgium", "France", "Italy", "Germany", "Spain","Hanoi",
        "Ho Chi Minh City",
        "Tokyo",
        "New York",
        "London",
        "Paris",
        "Sydney",
        "Berlin",
        "Moscow"
    )

    private lateinit var recyclerView: RecyclerView
    private val selectedItems = mutableSetOf<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.city_management_layout)

        txt_SelectedItem = findViewById(R.id.txt_SelectedItem)
        txt_management = findViewById(R.id.txt_management)
        editTextSearch = findViewById(R.id.editTextSearch)
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
        // Khởi tạo Adapter và gán cho AutoCompleteTextView
        //val suggestions = mutableListOf<String>() // Danh sách gợi ý tìm kiếm
        autoCompleteAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, COUNTRIES)
        editTextSearch.setAdapter(autoCompleteAdapter)

//        editTextSearch.addTextChangedListener(object :TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                val searchQuery=p0.toString().trim()
//                if (searchQuery.isNotEmpty()){
//                    performAutocompleteSearch(searchQuery)
//                    autoCompleteAdapter.notifyDataSetChanged()
//                    Log.e("citySuggestions","Đã thay đổi dữ liệu")
//                }else if (searchQuery.isEmpty()){
//                    // Nếu trường AutoCompleteTextView rỗng, xoá danh sách gợi ý tìm kiếm
//                    citySuggestions.clear()
//                    Log.e("citySuggestions","Đã xoá danh sách")
//                    Log.e("citySuggestions",citySuggestions.toString())
//                    //autoCompleteAdapter.notifyDataSetChanged()
//                }
//
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                // Cập nhật lại Adapter sau khi thay đổi văn bản
//                autoCompleteAdapter.notifyDataSetChanged()
//            }
//
//        })
    }

    private fun performAutocompleteSearch(searchQuery: String) {
        citySuggestions.clear()
        Log.e("searchQuery",searchQuery)

        // Fake dữ liệu tên thành phố - đây chỉ là ví dụ, bạn có thể sử dụng dữ liệu thực từ API
        val fakeCities = listOf(
            "Hanoi",
            "Ho Chi Minh City",
            "Tokyo",
            "New York",
            "London",
            "Paris",
            "Sydney",
            "Berlin",
            "Moscow"
        )

        // Tìm kiếm các thành phố khớp với query và thêm vào danh sách gợi ý tìm kiếm
        for (city in fakeCities) {
            if (city.startsWith(searchQuery, ignoreCase = true)) {
                citySuggestions.add(city)
                Log.e("citySuggestions",citySuggestions.toString())
            }
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