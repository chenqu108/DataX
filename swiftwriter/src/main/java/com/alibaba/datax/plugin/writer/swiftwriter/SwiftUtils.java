package com.alibaba.datax.plugin.writer.swiftwriter;

import com.alibaba.datax.common.element.Record;
import com.alibaba.datax.common.exception.DataXException;
import com.alibaba.datax.common.util.Configuration;
import com.alibaba.search.swift.SwiftAdminAdaptor;
import com.alibaba.search.swift.SwiftClient;
import com.alibaba.search.swift.exception.SwiftException;
import com.alibaba.search.swift.protocol.AdminRequestResponse;
import com.alibaba.search.swift.protocol.ErrCode;
import com.google.protobuf.ByteString;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by zw86077 on 2015/12/9.
 */
public class SwiftUtils {


    private final static Logger LOG = LoggerFactory.getLogger(SwiftUtils.class);


    public static final String CMD_SEPARATOR = (char) 0x1E + "\n";

    public static final String FIELD_SEPARATOR = (char) 0x1F + "\n";


    /**
     * 创建swift topic
     *
     * @param swiftClient
     * @param topicName
     * @param partitionNum
     * @throws SwiftException
     */

    public static int createTopicIfNotExists(SwiftClient swiftClient, String topicName, int partitionNum) throws SwiftException {
        AdminRequestResponse.TopicCreationRequest.Builder request = AdminRequestResponse.TopicCreationRequest
                .newBuilder();

        request.setTopicName(topicName);
        request.setPartitionCount(partitionNum);
        SwiftAdminAdaptor adminAdapter = swiftClient.getAdminAdapter();


        ErrCode.ErrorCode code = null;

        try {
            AdminRequestResponse.TopicInfoResponse topicInfo = adminAdapter.getTopicInfo(topicName);
            LOG.info("check topic OK|topic=" + topicName + ",partition=" + topicInfo.getTopicInfo().getPartitionCount());
            return topicInfo.getTopicInfo().getPartitionCount();
        } catch (SwiftException e) {
            code = e.getEc();

            if (code != ErrCode.ErrorCode.ERROR_ADMIN_TOPIC_NOT_EXISTED) {
                throw e;
            }
        }

        if (code == ErrCode.ErrorCode.ERROR_ADMIN_TOPIC_NOT_EXISTED) {
            LOG.info("create new topic success|topic=" + topicName + ",partition=" + partitionNum);
            adminAdapter.createTopic(request.build());
            return partitionNum;
        }


        //not reach here
        return 0;


    }


    /**
     * 将 record 转换成 doc
     *
     * @param record
     * @param indexes
     * @return
     */
    public static String record2Doc(Record record, List<String> indexes) {
        if (indexes.size() != record.getColumnNumber()) {
            throw DataXException.asDataXException(SwiftWriterErrorCode.ILLEGAL_VALUE, "数据记录字段数与索引个数不匹配,字段数：" +
                    record.getColumnNumber() + "" +
                    "索引个数:" + indexes.size());
        }


        StringBuilder buf = new StringBuilder("CMD=add").append(FIELD_SEPARATOR);
        for (int i = 0; i < indexes.size(); i++) {
            buf.append(indexes.get(i)).append("=").append(record.getColumn(i).asString()).append(FIELD_SEPARATOR);
        }


        buf.append(CMD_SEPARATOR);

        return buf.toString();

    }


    public static String parseHashStr(Record record) {
        return record.getColumn(0).asString();
    }




    /**
     * 从writer config 中解析出 topic name
     *
     * @param writeConfig
     * @return
     */
    public static String extractTopicNameFromWriterConfig(String writeConfig) {
        if (StringUtils.isBlank(writeConfig)) {
            return "";
        }

        String topicName = StringUtils.substringBetween(writeConfig, "topicName=", ";");
        return topicName;
    }
}
