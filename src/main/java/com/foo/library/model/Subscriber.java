package com.foo.library.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(value = Subscriber.SubscriberPK.class)
public class Subscriber {

	public static class SubscriberPK implements Serializable {

		private static final long serialVersionUID = -446433056128272472L;

		private String userId;

		@Enumerated(EnumType.STRING)
		// forces the enum to be stored as string than the ordinals
		private EventType eventType;

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}

		public EventType getEventType() {
			return eventType;
		}

		public void setEventType(EventType eventType) {
			this.eventType = eventType;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((eventType == null) ? 0 : eventType.hashCode());
			result = prime * result
					+ ((userId == null) ? 0 : userId.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			SubscriberPK other = (SubscriberPK) obj;
			if (eventType != other.getEventType())
				return false;
			if (userId == null) {
				if (other.getUserId() != null)
					return false;
			} else if (!userId.equals(other.getUserId()))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "SubscriberPK [userId=" + userId + ", eventType="
					+ eventType + "]";
		}

	}

	@Id
	private String userId;

	@Id
	@Enumerated(EnumType.STRING)
	private EventType eventType;

	public Subscriber() {
		super();
	}

	public Subscriber(String userId, EventType eventType) {
		this.eventType = eventType;
		this.userId = userId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventType == null) ? 0 : eventType.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Subscriber))
			return false;
		Subscriber other = (Subscriber) obj;
		if (eventType != other.getEventType())
			return false;
		if (userId == null) {
			if (other.getUserId() != null)
				return false;
		} else if (!userId.equals(other.getUserId()))
			return false;
		return true;
	}

}
