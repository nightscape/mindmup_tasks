package com.mindmup.android.tasks

import android.app._
import android.content.{ Intent, IntentSender, Context, SharedPreferences }
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.util.Log
import android.view._
import android.widget._
import macroid._
import macroid.contrib._
import macroid.FullDsl._
import macroid.IdGeneration
import macroid.viewable._
import macroid.contrib.LpTweaks._
import CustomTweaks._
import android.support.v4.app.Fragment

import rx._
import rx.ops._


class TaskListFragment[T, V <: View](currentTasks: Rx[Seq[T]], taskFilterString: Rx[String], queryInterpreter: CharSequence => (T => Boolean))(implicit val listable: Listable[T, V])
extends Fragment with Contexts[Fragment] with RxSupport with IdGeneration {
  import FilterableListableListAdapter._
  val itemSelections: Var[Option[T]] = Var[Option[T]](None)

  lazy val taskListView: Ui[ListView] = w[ListView] <~
    currentTasks.map(t => listable.filterableListAdapterTweak(t, queryInterpreter)) <~
    taskFilterString.map { fs =>
      Tweak[ListView] { lv =>
        val adapter = lv.getAdapter.asInstanceOf[ListableListAdapter[_, _]]
        if(adapter != null) {
          adapter.getFilter.filter(fs)
        }
      }
    } <~
    FuncOn.itemClick[ListView] { (_: AdapterView[_], _: View, index: Int, _: Long) =>
      val selectedTask = getUi(taskListView).getItemAtPosition(index).asInstanceOf[T]
      itemSelections()= Some(selectedTask)
      Ui(true)
    }

  override def onCreateView(inflater: LayoutInflater, container: ViewGroup, savedInstanceState: Bundle): View = {
    getUi { taskListView }
  }
}

object TaskListFragment {
  def newInstance[T, V <: View](currentTasks: Rx[Seq[T]], taskFilterString: Rx[String], queryInterpreter: CharSequence => (T => Boolean), listable: Listable[T, V]) =
    new TaskListFragment[T, V](currentTasks, taskFilterString, queryInterpreter)(listable)
}
