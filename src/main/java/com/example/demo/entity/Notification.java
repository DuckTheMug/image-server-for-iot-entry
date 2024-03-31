package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.SQLDelete;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "notification")
@SQLDelete(sql = "UPDATE DEMO.NOTIFICATION SET DELETED = true WHERE ID = ?")
@FilterDef(name = "deletedNotificationFilter", parameters = @ParamDef(name = "isDeleted", type = Boolean.class))
@Filter(name = "deletedNotificationFilter", condition = "deleted = :isDeleted")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long notificationId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "entry_id", referencedColumnName = "id", nullable = false)
    private Entry entry;

    @Column(name = "is_read", nullable = false)
    private Boolean isRead;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted = Boolean.FALSE;
}
