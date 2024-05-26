package buddha.qqbot;

public enum EventTypeEnum {
    /** 身份认证成功后返回的事件 */
    READY,
    /** 机器人加入新的GUILD */
    GUILD_CREATE,
    /** GUILD 资料发生变更 */
    GUILD_UPDATE,
    /** 机器人退出GUILD */
    GUILD_DELETE,
    /** CHANNEL被创建  */
    CHANNEL_CREATE,
    /** CHANNEL有更新 */
    CHANNEL_UPDATE,
    /** CHANNEL被删除 */
    CHANNEL_DELETE,
    /** 成员加入 */
    GUILD_MEMBER_ADD,
    /** 成员资料发生变更 */
    GUILD_MEMBER_UPDATE,
    /** 成员被移除 */
    GUILD_MEMBER_REMOVE,
    /** 发送消息时间 */
    MESSAGE_CREATE,
    /** 消息被撤回 */
    MESSAGE_DELETE,
    /** 未消息添加表情表态 */
    MESSAGE_REACTION_ADD,
    /** 未消息删除表情表态 */
    MESSAGE_REACTION_REMOVE,
    /** 收到用户发给机器人的私信消息 */
    DIRECT_MESSAGE_CREATE,
    /** 删除（撤回）消息事件 */
    DIRECT_MESSAGE_DELETE,
    /** 互动事件 */
    INTERACTION_CREATE,
    /** 消息审核通过 */
    MESSAGE_AUDIT_PASS,
    /** 消息审核失败 */
    MESSAGE_AUDIT_REJECT,
    /** 用户创建主题时 */
    FORUM_THREAD_CREATE,
    /** 用户更新主题时 */
    FORUM_THREAD_UPDATE,
    /** 用户删除主体时 */
    FORUM_THREAD_DELETE,
    /** 用户创建帖子时 */
    FORUM_POST_CREATE,
    /** 用户删除帖子时 */
    FORUM_POST_DELETE,
    /** 用户回复评论时 */
    FORUM_REPLY_CREATE,
    /** 用户删除评论时 */
    FORUM_REPLY_DELETE,
    /** 用户发表审核通过时 */
    FORUM_PUBLISH_AUDIT_RESULT,
    /** 音频开始播放时 */
    AUDIO_START,
    /** 音频播放结束时 */
    AUDIO_FINISH,
    /** 上麦时 */
    AUDIO_ON_MIC,
    /** 下麦 */
    AUDIO_OFF_MIC,
    /** 当机器人收到@消息时 */
    AT_MESSAGE_CREATE,
    /** 频道消息被删除时 */
    PUBLIC_MESSAGE_DELETE
    ;
}
