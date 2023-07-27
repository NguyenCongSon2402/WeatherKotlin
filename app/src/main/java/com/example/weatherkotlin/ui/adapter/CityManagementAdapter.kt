package com.example.weatherkotlin.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherkotlin.R
import com.example.weatherkotlin.interfaces.RecyclerViewItemClickListener
import java.lang.Exception

class CityManagementAdapter(
    private val itemLongClickListener: RecyclerViewItemClickListener,
    private val txt_SelectedItem: TextView
) :
    RecyclerView.Adapter<CityManagementAdapter.CityHolder>() {
    private lateinit var context: Context
    private var isEditMode: Boolean = false
    private val location = arrayListOf("Nam định", "Hà Nội", "Thái bình", "Hoàng mai")
    private val temperature = arrayListOf("31°", "31°", "31°", "31°")
    private val temperature1 = arrayListOf("31°/25°", "31°/25°", "31°/25°", "31°/25°")
    private val selectedItems = mutableListOf<Int>()

    inner class CityHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var txt_location: TextView = itemView.findViewById(R.id.txt_location)
        var txt_temperature1: TextView = itemView.findViewById(R.id.txt_temperature1)
        var txt_temperature: TextView = itemView.findViewById(R.id.txt_temperature)
        var checkBoxDelete: CheckBox = itemView.findViewById(R.id.checkBoxDelete)

        init {
            // Xử lý sự kiện nhấn giữ lâu trên item
            itemView.setOnLongClickListener {
                itemLongClickListener.onItemLongClick(adapterPosition)
                true // Trả về 'true' để xác định rằng sự kiện đã được xử lý
            }
            checkBoxDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    if (checkBoxDelete.isChecked) {
                        // Nếu checkbox được chọn, thêm vị trí vào danh sách selectedItems
                        if (!selectedItems.contains(position)) {
                            selectedItems.add(position)
                        }
                    } else {
                        // Nếu checkbox được bỏ chọn, loại bỏ vị trí khỏi danh sách selectedItems
                        selectedItems.remove(position)
                    }

                    // Hiển thị danh sách các thành phố đã chọn trực tiếp trên giao diện
                    val selectedText = "Các mục đã chọn:\n" + selectedItems.joinToString(", ")
                    txt_SelectedItem.text = selectedText
                }
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return CityHolder(view)
    }

    override fun getItemCount(): Int {
        if (location != null)
            return location.size
        return 0
    }

    override fun onBindViewHolder(holder: CityHolder, position: Int) {
        holder.txt_location.text = location[position]
        holder.txt_temperature1.text = temperature1[position]
        holder.txt_temperature.text = temperature[position]

        holder.checkBoxDelete.isChecked = selectedItems.contains(position)
        if (isEditMode) {
            holder.checkBoxDelete.visibility = View.VISIBLE
        } else {
            holder.checkBoxDelete.visibility = View.GONE
        }
    }

    fun setEditMode(isEditMode: Boolean) {
        this.isEditMode = isEditMode
        notifyDataSetChanged()
    }

    // Function to update selected item positions
    fun setSelectedItems(selectedItems: List<Int>) {
        this.selectedItems.clear()
        this.selectedItems.addAll(selectedItems)
        val selectedText = "Các mục đã chọn:\n" + selectedItems.joinToString(", ")
        txt_SelectedItem.text = selectedText
        notifyDataSetChanged()
    }

    fun clearSelectedItems() {
        selectedItems.clear()
        val selectedText = "Các mục đã chọn:\n" + selectedItems.joinToString(", ")
        txt_SelectedItem.text = selectedText
        notifyDataSetChanged()
    }

    // Function to get the list of selected item positions
    fun getSelectedItems(): List<Int> {
        return selectedItems
    }

    fun deleteItemSelected() {
        try {
            // Sử dụng hàm reversed() để đảo ngược thứ tự của danh sách, giúp xóa các phần tử từ cuối danh sách về đầu.
            for (position in selectedItems.reversed()) {
                location.removeAt(position)
                temperature.removeAt(position)
                temperature1.removeAt(position)
            }
            selectedItems.clear() // Xóa tất cả các phần tử đã chọn
        } catch (e: Exception) {
            e.printStackTrace()
        }
        isEditMode = false
        // Cập nhật lại RecyclerView sau khi xóa các item đã chọn
        notifyDataSetChanged()
    }


}