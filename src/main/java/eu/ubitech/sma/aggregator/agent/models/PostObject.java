package eu.ubitech.sma.aggregator.agent.models;

import java.util.Map;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author promitheas
 */
public class PostObject {

    private final long currentEpoch;
    private final String posts;
    private final int postsNum;
    private final Map<Long,EntityObject> entities;
    public long getCurrentEpoch() {
        return currentEpoch;
    }

    public String getPosts() {
        return posts;
    }

    public int getPostsNum() {
        return postsNum;
    }

    public Map<Long, EntityObject> getEntities() {
        return entities;
    }
    
  

    public PostObject(long _epoch, String _posts, int _postsNum,Map<Long,EntityObject> _entities) {
        currentEpoch = _epoch;
        posts = _posts;
        postsNum = _postsNum;
        entities = _entities;
    }

}
