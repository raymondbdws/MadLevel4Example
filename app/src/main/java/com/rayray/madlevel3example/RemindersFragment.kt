package com.rayray.madlevel3example

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_reminders.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class RemindersFragment : Fragment() {

    private lateinit var reminderRepository: ReminderRepository

    private val REMINDERS = arrayListOf<Reminder>()
    private val reminderAdapter = ReminderAdapter(REMINDERS)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reminders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        observeAddReminderResult();

        reminderRepository = ReminderRepository(requireContext())
        getRemindersFromDatabase()
    }

    private fun getRemindersFromDatabase() {
        val reminders = reminderRepository.getAllReminders()
        this@RemindersFragment.reminders
    }

    private fun initViews() {
        rvReminders.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvReminders.adapter = reminderAdapter
 
        createItemTouchHelper().attachToRecyclerView(rvReminders)
    }

    private fun observeAddReminderResult() {
        setFragmentResultListener(REQ_REMINDER_KEY) { key, bundle ->
            bundle.getString(BUNDLE_REMINDER_KEY)?.let {
                val REMINDER = Reminder(it)
                REMINDERS.add(REMINDER)
                reminderAdapter.notifyDataSetChanged()
            }?: Log.e("ReminderFragment", "Request triggered, but empty reminder text!")
        }
    }

    private fun createItemTouchHelper(): ItemTouchHelper{
        val CALLBACK = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val POSITION = viewHolder.adapterPosition
                REMINDERS.removeAt(POSITION)
                reminderAdapter.notifyDataSetChanged()
            }

        }
        return ItemTouchHelper(CALLBACK)
    }
}