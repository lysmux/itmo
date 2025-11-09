package dev.lysmux.tags;

import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import lombok.extern.java.Log;

import java.io.IOException;

@Log
@FacesComponent("columnTag")
public class ColumnTag extends UIComponentBase {
    public static final String COMPONENT_FAMILY = "org.lysmux.table";

    enum PropertyKeys {
        header, property, sortable
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getHeader() {
        return (String) getStateHelper().eval(PropertyKeys.header);
    }

    public void setHeader(String header) {
        getStateHelper().put(PropertyKeys.header, header);
    }

    public Object getProperty() {
        return getStateHelper().eval(PropertyKeys.property);
    }

    public void setProperty(String property) {
        getStateHelper().put(PropertyKeys.property, property);
    }

    public boolean isSortable() {
        return (boolean) getStateHelper().eval(PropertyKeys.sortable, false);
    }

    public void setSortable(boolean sortable) {
        getStateHelper().put(PropertyKeys.sortable, sortable);
    }

    public void encodeCellValue(FacesContext context) throws IOException {
        ResponseWriter writer = context.getResponseWriter();

        if (getProperty() != null) {
            Object cellValue = getProperty();
            writer.write(cellValue != null ? cellValue.toString() : "");
        } else {
            encodeChildren(context);
        }
    }
}
