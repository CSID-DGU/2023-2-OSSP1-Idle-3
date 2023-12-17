package com.almostThere.domain.map.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;

@Data
@NoArgsConstructor
public class UserLocation implements Serializable {

    private long memberId;
    private String memberNickName;
    private double[] memberLatLng;

    public UserLocation(long memberId, String memberNickName, double[] memberLatLng){
        this.memberId = memberId;
        this.memberNickName = memberNickName;
        this.memberLatLng = memberLatLng;
    }

}

