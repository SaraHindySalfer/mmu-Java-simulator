package main.java.com.company.hit.dm;


import java.util.Objects;

/**
 * class that simulates memory page structure
 * @param <T> page value type
 */
public class DataModel<T> extends java.lang.Object implements java.io.Serializable
{
    private Long id;
    private T content;

    public DataModel() {
        super();
    }

    public DataModel(Long id,T content) {
        this.id=id;
        this.content=content;
    }
    public T getContent() {
        return content;
    }
    public void setContent(T content) {
        this.content = content;
    }

    public Long getDataModelId() {
        return id;
    }

    public void setDataModelId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DataModel<?> dataModel = (DataModel<?>) o;
        return id.equals(dataModel.id);
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "id=" + id +
                ", content=" + content +
                '}';
    }
}
