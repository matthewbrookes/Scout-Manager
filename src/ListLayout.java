import java.awt.Container;
import java.awt.GridLayout;
import java.awt.Insets;

/**
 * This class extends GridLayout and provides an interface
 * for displaying names in columns which are filled top-bottom
 * rather than the default behaviour of filling across the row
 * @author Matthew Brookes
 */
public class ListLayout extends GridLayout{
	/**
	 * @param i The number of columns in layout
	 * @param j The number of rows in layout
	 */
	public ListLayout(int i, int j) {
		// Set the number of rows and columns
		this.setColumns(i);
		this.setRows(j);
	}

	@Override
	public void layoutContainer(Container parent) {
		/* The following code determines the appearance of the layout
		 * and is almost identical to the stock java code in GridLayout
		 * class from the system library. 
		 */
		
		  synchronized (parent.getTreeLock()) {
		    Insets insets = parent.getInsets();
		    int ncomponents = parent.getComponentCount();
		    int nrows = this.getRows();
		    int ncols = this.getColumns();
		    boolean ltr = parent.getComponentOrientation().isLeftToRight();

		    if (ncomponents == 0) {
		        return;
		    }
		    if (nrows > 0) {
		        ncols = (ncomponents + nrows - 1) / nrows;
		    } else {
		        nrows = (ncomponents + ncols - 1) / ncols;
		    }
		    int w = parent.getWidth() - (insets.left + insets.right);
		    int h = parent.getHeight() - (insets.top + insets.bottom);
		    w = (w - (ncols - 1) * this.getHgap()) / ncols;
		    h = (h - (nrows - 1) * this.getVgap()) / nrows;

		    if (ltr) {
		        for (int c = 0, x = insets.left ; c < ncols ; c++, x += w + this.getHgap()) {
		        for (int r = 0, y = insets.top ; r < nrows ; r++, y += h + this.getVgap()) {
		        	int i = c * nrows + r; // This line changes the order that the labels are drawn in
		            if (i < ncomponents) {
		            parent.getComponent(i).setBounds(x, y, w, h);
		            }
		        }
		        }
		    } else {
		        for (int c = 0, x = parent.getWidth() - insets.right - w; c < ncols ; c++, x -= w + this.getHgap()) {
		        for (int r = 0, y = insets.top ; r < nrows ; r++, y += h + this.getVgap()) {
		        	int i = c * nrows + r; // This line changes the order that the labels are drawn in
		            if (i < ncomponents) {
		            parent.getComponent(i).setBounds(x, y, w, h);
		            }
		        }
		        }
		    }
		  }
		}
}
