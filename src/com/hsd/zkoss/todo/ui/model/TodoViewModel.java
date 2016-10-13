package com.hsd.zkoss.todo.ui.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import org.apache.log4j.Logger;
import org.zkoss.bind.Binder;
import org.zkoss.bind.DefaultBinder;
import org.zkoss.bind.annotation.AfterCompose;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.ContextParam;
import org.zkoss.bind.annotation.ContextType;
import org.zkoss.bind.annotation.GlobalCommand;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.DropEvent;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.select.Selectors;
import org.zkoss.zk.ui.select.annotation.Listen;
import org.zkoss.zk.ui.select.annotation.Wire;
import org.zkoss.zul.ListModelList;
import org.zkoss.zul.Listbox;

import com.hsd.zkoss.todo.auth.AuthenticationService;
import com.hsd.zkoss.todo.auth.AuthenticationServiceImpl;
import com.hsd.zkoss.todo.model.TodoItem;

public class TodoViewModel {

	private static Logger logger = Logger.getLogger(TodoViewModel.class);

	private Binder binder = new DefaultBinder();

	// services
	private AuthenticationService authService = new AuthenticationServiceImpl();

	private AddForm addForm = new AddForm();
	private List<TodoItem> todos = new ArrayList();
	{
		for (int i = 0; i < 10; i++) {
			todos.add(new TodoItem(UUID.randomUUID().toString(), true, "title" + i, i));
		}
	}

	@Wire("#todoList")
	private Listbox todoList;
	private ListModelList<TodoItem> todoListModel;

	public AddForm getAddForm() {
		return addForm;
	}

	public void setAddForm(AddForm addForm) {
		this.addForm = addForm;
	}

	public List<TodoItem> getTodos() {
		return todos;
	}

	public void setTodos(List<TodoItem> todos) {
		this.todos = todos;
	}

	@AfterCompose
	public void afterCompose(@ContextParam(ContextType.VIEW) Component view) {
		logger.info("afterCompose()");
		Selectors.wireComponents(view, this, false);
	}

	@Command("onCreate")
	public void onCreate() {
		logger.info("loadModel() [addForm.addText:" + addForm.getAddText() + "]");

		todoListModel = new ListModelList(todos);
		todoList.setModel(todoListModel);
	}

	@Command("addTodo")
	public void addTodo() {
		logger.info("addTodo() [addForm.addText:" + addForm.getAddText() + "] [todos.size:" + todos.size() + "]");
		addTodo(addForm.getAddText());

		prepareTodos();
	}

	@Command("completeTodo")
	public void completeTodo(@BindingParam("target") Component target) {
		logger.info("completeTodo() [target:" + target + "] [id:" + target.getId() + "]");
		markComplete(target.getId().substring("active_".length()));

		prepareTodos();
	}

	@Command("onDrop")
	public void onDrop(@BindingParam("event") Event event) {
		logger.info("onDrop() [event.dragged.id:" + ((DropEvent) event).getDragged().getId() + "] [target:" + ((DropEvent) event).getTarget().getId() + "]");
		dragdrop(((DropEvent) event).getDragged().getId(), ((DropEvent) event).getTarget().getId());

		 prepareTodos();
	}

	@Command("logout")
	public void logout() {
		logger.info("logout()");
		authService.logout();
		Executions.sendRedirect("/index.zul");
	}
	
	// ******************** utils *********************

	private void prepareTodos() {
		Collections.sort(todos, new Comparator<TodoItem>() {
			@Override
			public int compare(TodoItem todo1, TodoItem todo2) {
				if (todo1.getIsActive() != todo2.getIsActive()) {
					return todo2.getIsActive().compareTo(todo1.getIsActive());
				} else {
					return todo1.getOrder().compareTo(todo2.getOrder());
				}
			}
		});

		Integer todoIndex = 0;
		for (TodoItem todo : todos) {
			if (todo.getIsActive()) {
				todo.setOrder(todoIndex++);
			}
		}
		todoIndex = 0;
		for (TodoItem todo : todos) {
			if (!todo.getIsActive()) {
				todo.setOrder(todoIndex++);
			}
		}

		todoListModel = new ListModelList(todos);
		todoList.setModel(todoListModel);
	}

	private void addTodo(String title) {
		Integer activeCount = 0;
		for (TodoItem todoItem : todos) {
			if (todoItem.getIsActive()) {
				activeCount++;
			}
		}
		todos.add(new TodoItem(UUID.randomUUID().toString(), true, title, activeCount));
	}

	private void markComplete(String id) {
		for (TodoItem todo : todos) {
			if (todo.getId().equals(id)) {
				todo.setIsActive(false);
				todo.setOrder(-1);
				break;
			}
		}
	}

	private void dragdrop(String draggedId, String droppedId) {
		draggedId = draggedId.substring("cont_".length());
		droppedId = droppedId.substring("cont_".length());
		TodoItem todoDragged = null, todoDropped = null;
		for (TodoItem todo : todos) {
			if (todo.getId().equals(draggedId)) {
				todoDragged = todo;
			} else if (todo.getId().equals(droppedId)) {
				todoDropped = todo;
			}
		}
		logger.info("dragdrop() [dragged:" + (todoDragged == null ? "ISNULL" : todoDragged.getOrder()) + "] [dropped:"
				+ (todoDropped == null ? "ISNULL" : todoDropped.getOrder()) + "]");
		Integer orderShift = todoDragged.getOrder() > todoDropped.getOrder() ? 1 : -1;
		Integer droppedOrder = todoDropped.getOrder();
		for (TodoItem todo : todos) {
			if (todo.getOrder() >= Math.min(todoDragged.getOrder() + 1, todoDropped.getOrder())
					&& todo.getOrder() <= Math.max(todoDragged.getOrder() - 1, todoDropped.getOrder())) {
				todo.setOrder(todo.getOrder() + orderShift);
			}
		}
		todoDragged.setOrder(droppedOrder);
	}

}
