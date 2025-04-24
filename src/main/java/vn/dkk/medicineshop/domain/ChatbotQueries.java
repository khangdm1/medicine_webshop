package vn.dkk.medicineshop.domain;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "chatbot_queries")
public class ChatbotQueries {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // @Column(columnDefinition = "MEDIUMTEXT")
    private String query;

    // @Column(columnDefinition = "MEDIUMTEXT")
    private String response;
    private Date created_at;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return "ChatbotQueries [id=" + id + ", query=" + query + ", response=" + response + ", created_at=" + created_at
                + "]";
    }

}
