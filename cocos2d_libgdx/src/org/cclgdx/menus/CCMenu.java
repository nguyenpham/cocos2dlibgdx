package org.cclgdx.menus;

import java.util.ArrayList;

import org.cclgdx.config.ccMacros;
import org.cclgdx.director.CCDirector;
import org.cclgdx.events.CCMotionEvent;
import org.cclgdx.events.CCTouchDispatcher;
import org.cclgdx.layers.CCLayer;
import org.cclgdx.nodes.CCNode;
import org.cclgdx.types.CGPoint;
import org.cclgdx.types.CGSize;

/**
 * CCMenu
 * 
 */
public class CCMenu extends CCLayer {
	enum MenuState {
		kMenuStateWaiting, kMenuStateTrackingTouch
	}

	private static final int kDefaultPadding = 5;

	private CCMenuItem selectedItem;

	private MenuState state;

	public CCMenuItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(CCMenuItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	@Override
	protected void registerWithTouchDispatcher() {
		CCTouchDispatcher.sharedDispatcher().addTargetedDelegate(this, ccMacros.INT_MIN + 1, true);
	}

	/** creates a menu with its items */
	public static CCMenu menu(CCMenuItem... items) {
		return new CCMenu(items);
	}

	public static CCMenu menu() {
		return new CCMenu((CCMenuItem[]) null);
	}

	/** initializes a CCMenu with it's items */
	protected CCMenu(CCMenuItem... items) {
		super();
		isTouchEnabled_ = true;

		if (items != null) {
			for (int i = 0; i < items.length; i++) {
				CCMenuItem item = items[i];
				addChild(item, i);
			}
		}

		selectedItem = null;
		state = MenuState.kMenuStateWaiting;
	}

	/*
	 * override add:
	 */
	@Override
	public CCNode addChild(CCNode child, int z, int tag) {
		return super.addChild(child, z, tag);
	}

	// Menu - Events
	@Override
	public boolean ccTouchesBegan(CCMotionEvent event) {
		if (state != MenuState.kMenuStateWaiting || !visible_) {
			return CCTouchDispatcher.kEventIgnored;
		}

		selectedItem = itemForTouch(event);
		if (selectedItem != null) {
			selectedItem.selected();
			state = MenuState.kMenuStateTrackingTouch;
			return CCTouchDispatcher.kEventHandled;
		}

		return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	public boolean ccTouchesEnded(CCMotionEvent event) {
		if (state == MenuState.kMenuStateTrackingTouch) {
			if (selectedItem != null) {
				selectedItem.unselected();
				selectedItem.activate();
			}

			state = MenuState.kMenuStateWaiting;
			return CCTouchDispatcher.kEventHandled;
		}

		return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	public boolean ccTouchesCancelled(CCMotionEvent event) {
		if (state == MenuState.kMenuStateTrackingTouch) {
			if (selectedItem != null) {
				selectedItem.unselected();
			}

			state = MenuState.kMenuStateWaiting;
			return CCTouchDispatcher.kEventHandled;
		}

		return CCTouchDispatcher.kEventIgnored;
	}

	@Override
	public boolean ccTouchesMoved(CCMotionEvent event) {
		if (state == MenuState.kMenuStateTrackingTouch) {
			CCMenuItem currentItem = itemForTouch(event);

			if (currentItem != selectedItem) {
				if (selectedItem != null) {
					selectedItem.unselected();
				}
				selectedItem = currentItem;
				if (selectedItem != null) {
					selectedItem.selected();
				}
			}
			return CCTouchDispatcher.kEventHandled;
		}

		return CCTouchDispatcher.kEventIgnored;
	}

	// Menu - Alignment

	/**
	 * align items vertically
	 */
	public void alignItemsVertically() {
		alignItemsVertically(kDefaultPadding);
	}

	/**
	 * align items vertically with padding
	 */
	public void alignItemsVertically(float padding) {
		float height = -padding;
		for (CCNode item : children_) {
			height += item.getContentSize().height * item.getScaleY() + padding;
		}

		float y = height / 2.0f;
		for (int i = 0; i < children_.size(); i++) {
			CCMenuItem item = (CCMenuItem) children_.get(i);
			item.setPosition(0, y - item.getContentSize().height * item.getScaleY() / 2.0f);
			y -= item.getContentSize().height * item.getScaleY() + padding;
		}
	}

	/**
	 * align items horizontally
	 */
	public void alignItemsHorizontally() {
		alignItemsHorizontally(kDefaultPadding);
	}

	/**
	 * align items horizontally with padding
	 */
	public void alignItemsHorizontally(float padding) {
		float width = -padding;
		for (CCNode item : children_) {
			width += item.getContentSize().width * item.getScaleX() + padding;
		}

		float x = -width / 2.0f;
		for (CCNode item : children_) {
			item.setPosition(CGPoint.make(x + item.getContentSize().width * item.getScaleX() / 2.0f, 0));
			x += item.getContentSize().width * item.getScaleX() + padding;
		}
	}

	/**
	 * align items in rows of columns
	 */
	public void alignItemsInColumns(int columns[]) {
		ArrayList<Integer> rows = new ArrayList<Integer>();
		for (int i = 0; i < columns.length; i++) {
			rows.add(columns[i]);
		}

		int height = -5;
		int row = 0, rowHeight = 0, columnsOccupied = 0, rowColumns;
		for (int i = 0; i < children_.size(); i++) {
			CCMenuItem item = (CCMenuItem) children_.get(i);
			assert row < rows.size() : "Too many menu items for the amount of rows/columns.";

			rowColumns = rows.get(row);
			assert rowColumns != 0 : "Can't have zero columns on a row";

			rowHeight = (int) Math.max(rowHeight, item.getContentSize().height);
			++columnsOccupied;

			if (columnsOccupied >= rowColumns) {
				height += rowHeight + 5;

				columnsOccupied = 0;
				rowHeight = 0;
				++row;
			}
		}

		assert columnsOccupied != 0 : "Too many rows/columns for available menu items.";

		CGSize winSize = CCDirector.sharedDirector().winSize();

		row = 0;
		rowHeight = 0;
		rowColumns = 0;
		float w = 0, x = 0, y = height / 2;
		for (int i = 0; i < children_.size(); i++) {
			CCMenuItem item = (CCMenuItem) children_.get(i);
			if (rowColumns == 0) {
				rowColumns = rows.get(row);
				w = winSize.width / (1 + rowColumns);
				x = w;
			}

			rowHeight = Math.max(rowHeight, (int) item.getContentSize().height);
			item.setPosition(CGPoint.make(x - winSize.width / 2, y - item.getContentSize().height / 2));

			x += w + 10;
			++columnsOccupied;

			if (columnsOccupied >= rowColumns) {
				y -= rowHeight + 5;

				columnsOccupied = 0;
				rowColumns = 0;
				rowHeight = 0;
				++row;
			}
		}
	}

	/**
	 * align items in columns of rows
	 */
	public void alignItemsInRows(int rows[]) {
		ArrayList<Integer> columns = new ArrayList<Integer>();
		for (int i = 0; i < rows.length; i++) {
			columns.add(rows[i]);
		}

		ArrayList<Integer> columnWidths = new ArrayList<Integer>();
		ArrayList<Integer> columnHeights = new ArrayList<Integer>();

		int width = -10, columnHeight = -5;
		int column = 0, columnWidth = 0, rowsOccupied = 0, columnRows;
		for (int i = 0; i < children_.size(); i++) {
			CCMenuItem item = (CCMenuItem) children_.get(i);
			assert column < columns.size() : "Too many menu items for the amount of rows/columns.";

			columnRows = columns.get(column);
			assert columnRows != 0 : "Can't have zero rows on a column";

			columnWidth = (int) Math.max(columnWidth, item.getContentSize().width);
			columnHeight += item.getContentSize().height + 5;
			++rowsOccupied;

			if (rowsOccupied >= columnRows) {
				columnWidths.add(columnWidth);
				columnHeights.add(columnHeight);
				width += columnWidth + 10;

				rowsOccupied = 0;
				columnWidth = 0;
				columnHeight = -5;
				++column;
			}
		}

		assert rowsOccupied != 0 : "Too many rows/columns for available menu items.";

		CGSize winSize = CCDirector.sharedDirector().winSize();

		column = 0;
		columnWidth = 0;
		columnRows = 0;
		float x = -width / 2, y = 0;
		for (int i = 0; i < children_.size(); i++) {
			CCMenuItem item = (CCMenuItem) children_.get(i);
			if (columnRows == 0) {
				columnRows = columns.get(column);
				y = columnHeights.get(column) + winSize.height / 2;
			}

			columnWidth = (int) Math.max(columnWidth, item.getContentSize().width);
			item.setPosition(CGPoint.make(x + columnWidths.get(column) / 2, y - winSize.height / 2));

			y -= item.getContentSize().height + 10;
			++rowsOccupied;

			if (rowsOccupied >= columnRows) {
				x += columnWidth + 5;

				rowsOccupied = 0;
				columnRows = 0;
				columnWidth = 0;
				++column;
			}
		}
	}

	private CCMenuItem itemForTouch(CCMotionEvent event) {
		if (children_ != null) {

			CGPoint aPoint = CGPoint.make(event.x, event.y);

			for (int i = 0; i < children_.size(); i++) {
				CCMenuItem item = (CCMenuItem) children_.get(i);
				if (item.getVisible() && item.isEnabled() && item.containsScreenPoint(aPoint)) {
					return item;
				}
			}
		}
		return null;
	}
}
