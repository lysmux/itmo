package dev.lysmux.tags;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.UIOutput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@FacesComponent("tableTag")
@Log
public class TableTag extends UIComponentBase {
    public static final String COMPONENT_FAMILY = "org.lysmux.table";

    private static final String SORT_COL_IDX_KEY = "sort:colIdx";
    private static final String SORT_ASC_KEY = "sort:asc";
    private static final String PAGE_KEY = "pagination:page";

    private int page = 1;
    private int sortColIdx = -1;
    private boolean sortAsc = true;

    enum PropertyKeys {
        value, var, paginated, itemsPerPage,
        styleClass, style
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public List<?> getValue() {
        return (List<?>) getStateHelper().eval(PropertyKeys.value);
    }

    public void setValue(List<?> value) {
        getStateHelper().put(PropertyKeys.value, value);
    }

    public String getVar() {
        return (String) getStateHelper().eval(PropertyKeys.var);
    }

    public void setVar(String var) {
        getStateHelper().put(PropertyKeys.var, var);
    }

    public void setPaginated(boolean paginated) {
        getStateHelper().put(PropertyKeys.paginated, paginated);
    }

    public boolean isPaginated() {
        return (boolean) getStateHelper().eval(PropertyKeys.paginated, false);
    }

    public int getItemsPerPage() {
        return (int) getStateHelper().eval(PropertyKeys.itemsPerPage, 10);
    }

    public void setItemsPerPage(int itemsPerPage) {
        getStateHelper().put(PropertyKeys.itemsPerPage, itemsPerPage);
    }

    public String getStyleClass() {
        return (String) getStateHelper().eval(PropertyKeys.styleClass);
    }

    public void setStyleClass(String styleClass) {
        getStateHelper().put(PropertyKeys.styleClass, styleClass);
    }

    public String getStyle() {
        return (String) getStateHelper().eval(PropertyKeys.style);
    }

    public void setStyle(String style) {
        getStateHelper().put(PropertyKeys.style, style);
    }

    public void saveSessionData(FacesContext context) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        String clientId = getClientId(context);
        sessionMap.put(Utils.buildKey(clientId, PAGE_KEY), page);
        sessionMap.put(Utils.buildKey(clientId, SORT_COL_IDX_KEY), sortColIdx);
        sessionMap.put(Utils.buildKey(clientId, SORT_ASC_KEY), sortAsc);
    }

    public void restoreSessionData(FacesContext context) {
        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        String clientId = getClientId(context);
        page = (int) sessionMap.getOrDefault(Utils.buildKey(clientId, PAGE_KEY), page);
        sortColIdx = (int) sessionMap.getOrDefault(Utils.buildKey(clientId, SORT_COL_IDX_KEY), sortColIdx);
        sortAsc = (boolean) sessionMap.getOrDefault(Utils.buildKey(clientId, SORT_ASC_KEY), sortAsc);
    }

    @Override
    public void decode(FacesContext context) {
        Map<String, String> params = context.getExternalContext().getRequestParameterMap();
        String clientId = getClientId(context);

        String pageParam = params.get(Utils.buildKey(clientId, PAGE_KEY));
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException ignored) {
            }
        }

        String sortColIdxParam = params.get(Utils.buildKey(clientId, SORT_COL_IDX_KEY));
        if (sortColIdxParam != null) {
            try {
                sortColIdx = Integer.parseInt(sortColIdxParam);
            } catch (NumberFormatException ignored) {
            }
        }

        String sortAscParam = params.get(Utils.buildKey(clientId, SORT_ASC_KEY));
        if (sortAscParam != null) {
            try {
                sortAsc = Boolean.parseBoolean(sortAscParam);
            } catch (NumberFormatException ignored) {
            }
        }
        saveSessionData(context);
    }

    @Override
    public void encodeAll(FacesContext context) throws IOException {
        restoreSessionData(context);
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", this);
        writer.writeAttribute("id", getClientId(), "id");

        encodeTable(context);

        if (isPaginated()) {
            int totalPages = Math.max(1, (int) Math.ceil((double) getValue().size() / getItemsPerPage()));
            if (page > totalPages) {
                page = 1;
                saveSessionData(context);
            }

            encodePagination(context, totalPages);
        }

        writer.endElement("div");
    }

    public void encodeTable(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", this);
        writer.writeAttribute("class", "table-container", null);

        writer.startElement("table", this);
        writer.writeAttribute("class", getStyleClass(), "class");
        writer.writeAttribute("style", getStyle(), "style");
        encodeHeader(context);
        encodeBody(context);
        writer.endElement("table");
        writer.endElement("div");
    }

    public void encodePagination(FacesContext context, int totalPages) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("div", this);
        writer.writeAttribute("class", "pagination", null);

        encodePaginationButton(context, "<<", 1, page == 1);
        encodePaginationButton(context, "<", page - 1, page == 1);
        encodePaginationButton(context, String.valueOf(page), page, true);
        encodePaginationButton(context, ">", page + 1, page == totalPages);
        encodePaginationButton(context, ">>", totalPages, page == totalPages);

        writer.endElement("div");
    }

    public void encodePaginationButton(FacesContext context, String symbol, int page, boolean disabled) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String ajaxScript = Utils.buildAjaxScript(getClientId(), Map.ofEntries(
                Map.entry(PAGE_KEY, String.valueOf(page))
        ));

        writer.startElement("a", this);
        writer.writeAttribute("href", "#", null);
        writer.writeAttribute("class", disabled ? "disabled" : null, null);
        writer.writeAttribute("onclick", ajaxScript, null);
        writer.writeText(symbol, null);
        writer.endElement("a");
    }

    public void encodeHeader(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        writer.startElement("thead", this);
        writer.startElement("tr", this);

        for (int i = 0; i < getColumns().size(); i++) {
            ColumnTag column = getColumns().get(i);

            writer.startElement("th", this);
            writer.writeText(column.getHeader(), null);

            if (column.isSortable()) {
                String ajaxScript = Utils.buildAjaxScript(getClientId(), Map.ofEntries(
                        Map.entry(SORT_COL_IDX_KEY, String.valueOf(i)),
                        Map.entry(SORT_ASC_KEY, String.valueOf(sortColIdx != i || !sortAsc))
                ));
                writer.writeAttribute("onclick", ajaxScript, null);

                writer.startElement("span", this);
                writer.writeAttribute("class", "sort-indicator", null);
                writer.writeAttribute("style", "cursor: pointer;", null);

                if (sortColIdx == i) {
                    writer.writeText(sortAsc ? "↑" : "↓", null);
                } else {
                    writer.writeText("↕", null);
                }

                writer.endElement("span");
            }

            writer.endElement("th");
        }
        writer.endElement("tr");
        writer.endElement("thead");
    }

    public void encodeBody(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        List<?> sortedData = getSortedData(context);

        int fromItem = 0;
        int toItem = sortedData.size();
        if (isPaginated()) {
            fromItem = (page - 1) * getItemsPerPage();
            toItem = Math.min(fromItem + getItemsPerPage(), toItem);
        }

        writer.startElement("tbody", this);
        for (int i = fromItem; i < toItem; i++) {
            encodeRow(context, sortedData.get(i));
        }
        writer.endElement("tbody");
    }

    public void encodeRow(FacesContext context, Object obj) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        requestMap.put(getVar(), obj);
        writer.startElement("tr", this);

        for (ColumnTag column : getColumns()) {
            writer.startElement("td", this);
            column.encodeCellValue(context);
            writer.endElement("td");
        }

        requestMap.remove(getVar());
        writer.endElement("tr");
    }

    public List<ColumnTag> getColumns() {
        return getChildren().stream()
                .filter(ColumnTag.class::isInstance)
                .map(ColumnTag.class::cast)
                .collect(Collectors.toList());
    }

    public List<?> getSortedData(FacesContext context) {
        List<?> data = getValue();
        if (sortColIdx == -1) return data;

        ColumnTag column = getColumns().get(sortColIdx);
        return data.stream()
                .sorted((o1, o2) -> {
                    Object v1 = getCellValue(context, column, o1);
                    Object v2 = getCellValue(context, column, o2);
                    return compare(v1, v2);
                })
                .toList();
    }

    private Object getCellValue(FacesContext context, ColumnTag column, Object item) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        requestMap.put(getVar(), item);
        try {
            return column.getProperty() != null ?
                    column.getProperty() :
                    column.getChildren().stream()
                            .filter(UIOutput.class::isInstance)
                            .map(UIOutput.class::cast)
                            .map(UIOutput::getValue)
                            .filter(Objects::nonNull)
                            .map(Object::toString)
                            .collect(Collectors.joining());
        } finally {
            requestMap.remove(getVar());
        }
    }

    private int compare(Object v1, Object v2) {
        if (v1 == v2) return 0;
        if (v1 == null) return sortAsc ? -1 : 1;
        if (v2 == null) return sortAsc ? 1 : -1;

        @SuppressWarnings("unchecked")
        int result = v1 instanceof Comparable ?
                ((Comparable<Object>)v1).compareTo(v2) :
                String.valueOf(v1).compareTo(String.valueOf(v2));
        return sortAsc ? result : -result;
    }
}
