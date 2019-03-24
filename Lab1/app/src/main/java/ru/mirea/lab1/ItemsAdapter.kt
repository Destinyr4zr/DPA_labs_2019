package ru.mirea.lab1


import android.app.Activity
import android.content.Context
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ItemsAdapter(private var activity: Activity, numberQuantity: Int = 1000000): BaseAdapter() {

    private var numberOfElements = 0
    init { this.numberOfElements = numberQuantity }

    private class ViewHolder(row: View?) {
        var txtName: TextView? = null
        init { this.txtName = row?.findViewById(R.id.itemText) }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View?
        val viewHolder: ViewHolder
        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_item, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }
        view?.setBackgroundResource(if (position % 2 == 0) R.color.Darkcolor else R.color.Lightcolor)
        viewHolder.txtName?.setTextColor(ContextCompat.getColor(activity, if (position % 2 == 0) R.color.Lightcolor else R.color.Darkcolor))
        val resStr = "${NumConverter.convertToText(position + 1)}(${position + 1})"
        viewHolder.txtName?.text = resStr

        return view as View
    }

    override fun getItem(i: Int): String? {
        return null
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getCount(): Int {
        return numberOfElements
    }
}