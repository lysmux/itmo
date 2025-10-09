package dev.lysmux.tags.table;

import dev.lysmux.tags.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.JspFragment;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;
import lombok.Setter;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class DataTableTag extends SimpleTagSupport {
    @Setter
    private String var;

    @Setter
    private String cssClass;

    @Setter
    private String id;

    @Setter
    private String pageParam = "page";

    @Setter
    private int itemsPerPage = 10;

    @Setter
    private boolean paginated = false;

    private final List<Column> columns = new ArrayList<>();

    private List<?> items;

    public void setItems(List<?> items) {
        this.items = Objects.requireNonNullElseGet(items, List::of);
    }

    @Override
    public void doTag() throws JspException, IOException {
        this.id = this.id != null ? this.id : "table-container-" + Utils.generateHex();
        JspWriter out = getJspContext().getOut();

        PageContext pageContext = (PageContext) getJspContext();
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();

        getJspBody().invoke(null);

        if (paginated) {
            int totalPages = Math.max(1, (int) Math.ceil(items.size() / (double) itemsPerPage));
            int page = Optional.ofNullable(request.getParameter(pageParam))
                    .map(Integer::parseInt)
                    .orElse(1);
            page = Math.max(1, Math.min(page, totalPages));

            renderTable(out, page,  totalPages);
        } else {
            renderTable(out, null, null);
        }

        Utils.loadJS("table.js", out);
        out.println("<script>new TableSorter(\"%s\")</script>".formatted(id));
    }

    private void renderTable(JspWriter out, Integer page, Integer totalPages) throws JspException, IOException {
        int fromItem = 0;
        int toItem = items.size();

        out.print("<div");
        if (cssClass != null) out.print(" class='%s'".formatted(cssClass));
        out.println(">");

        out.println("<div class='_table-wrapper'>");
        out.print("<table id='%s'".formatted(id));
        if (paginated) {
            fromItem = (page - 1) * itemsPerPage;
            toItem = Math.min(fromItem + itemsPerPage, items.size());

            out.print(" data-page='%d'".formatted(page));
            out.print(" data-total-pages='%d'".formatted(totalPages));
        }
        out.print(">");

        out.print("<thead><tr>");
        for (Column column : columns) {
            out.print("<th data-property='%s'".formatted(column.property()));
            if (column.sortable()) {
                out.print(" data-sortable='true'");
            }
            out.print(">");
            out.print(column.name());

            if (column.sortable()) {
                out.print("<span style=\"cursor: pointer;\" class='_sort-indicator'>↕</span>");
            }

            out.print("</th>");
        }
        out.print("</tr></thead>");

        out.print("<tbody>");
        for (int i = fromItem; i < toItem; i++) {
            Object item = items.get(i);
            Class<?> clazz = item.getClass();

            out.print("<tr>");
            for (Column column : columns) {
                out.print("<td>");

                if (column.property() != null) {
                    try {
                        Field field = clazz.getDeclaredField(column.property());
                        field.setAccessible(true);
                        out.print(field.get(item).toString());
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        throw new JspException(e.getMessage());
                    }
                } else {
                    JspFragment body = column.body();
                    if (body != null) {
                        body.getJspContext().setAttribute(var, item);

                        StringWriter writer = new StringWriter();
                        body.invoke(writer);
                        out.print(writer.toString());
                    }
                }
                out.print("</td>");
            }
            out.print("</tr>");
        }
        out.print("</tbody>");
        out.print("</table>");
        out.println("</div>");

        if (paginated) {
            out.println("<div class='_pagination'>");
            out.println("<a class='_first-page %s' href=\"?page=1\">&lt;&lt;</a>".formatted(page == 1 ? "disabled" : ""));
            out.println("<a class='_previous-page %s' href=\"?page=%d\">&lt;</a>".formatted(page == 1 ? "disabled" : "", page - 1));
            out.println("<a class='_current-page'>%d</a>".formatted(page));
            out.println("<a class='_next-page %s' href=\"?page=%d\">&gt;</a>".formatted(page.equals(totalPages)? "disabled" : "", page + 1));
            out.println("<a class='_last-page %s' href=\"?page=%d\">&gt;&gt;</a>".formatted(page.equals(totalPages)? "disabled" : "", totalPages));
            out.println("</div>");
        }
        out.println("</div>");
    }

    public void addColumn(Column column) {
        columns.add(column);
    }

    public record Column(
            String name,
            String property,
            boolean sortable,
            JspFragment body
    ) {
    }
}
