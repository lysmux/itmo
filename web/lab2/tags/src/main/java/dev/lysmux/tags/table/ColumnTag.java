package dev.lysmux.tags.table;

import dev.lysmux.tags.table.DataTableTag;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import lombok.Setter;

import java.io.IOException;

public class ColumnTag extends SimpleTagSupport {
    @Setter
    private String header;

    @Setter
    private String property;

    @Setter
    private boolean sortable;

    @Override
    public void doTag() throws JspException, IOException {
        DataTableTag parent = (DataTableTag) findAncestorWithClass(this, DataTableTag.class);

        if (parent == null) {
            throw new JspException("Column must be inside a dataTable tag");
        }
        parent.addColumn(new DataTableTag.Column(header, property, sortable, getJspBody()));
    }
}
