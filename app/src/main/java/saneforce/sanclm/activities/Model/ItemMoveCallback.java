package saneforce.sanclm.activities.Model;

public class ItemMoveCallback  /*extends ItemTouchHelper.Callback*/ {

/*private final ItemTouchHelperContract mAdapter;
Context mContext;

public ItemMoveCallback(ItemTouchHelperContract adapter,Context mContext) {
        mAdapter = adapter;
        this.mContext=mContext;
        }

@Override
public boolean isLongPressDragEnabled() {
    Toast.makeText(mContext,"item_selected",Toast.LENGTH_SHORT).show();
        return true;
        }

@Override
public boolean isItemViewSwipeEnabled() {
        return false;
        }



@Override
public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

@Override
public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
    int dragFlags = ItemTouchHelper.UP   | ItemTouchHelper.DOWN |
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
    int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
        return makeMovementFlags(dragFlags, swipeFlags);
        }

@Override
public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
        RecyclerView.ViewHolder target) {
        mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
        }

@Override
public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
        int actionState) {


        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
        if (viewHolder instanceof PresentationRecyclerAdapter.ViewHolder) {
            PresentationRecyclerAdapter.ViewHolder myViewHolder=
        (PresentationRecyclerAdapter.ViewHolder) viewHolder;
        mAdapter.onRowSelected(myViewHolder);
        }

        }

        super.onSelectedChanged(viewHolder, actionState);
        }
@Override
public void clearView(RecyclerView recyclerView,
        RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);

        if (viewHolder instanceof PresentationRecyclerAdapter.ViewHolder) {
            PresentationRecyclerAdapter.ViewHolder myViewHolder=
        (PresentationRecyclerAdapter.ViewHolder) viewHolder;
        mAdapter.onRowClear(myViewHolder);
        }
        }

public interface ItemTouchHelperContract {

    void onRowMoved(int fromPosition, int toPosition);
    void onRowSelected(PresentationRecyclerAdapter.ViewHolder myViewHolder);
    void onRowClear(PresentationRecyclerAdapter.ViewHolder myViewHolder);

}*/

}
