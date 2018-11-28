package com.network.ping.mapper;

import com.network.ping.entity.PacketInfo;
import com.network.ping.entity.RTTData;
import lombok.experimental.UtilityClass;

import static com.network.ping.entity.PacketInfo.EMPTY_PACKET;
import static com.network.ping.util.TimeUtil.msOfMinute;

@UtilityClass
public class PacketMapper {
    private final int PAYLOAD_BLOCKS_AMOUNT = 4;
    private final static String DELIMITER = "@";
    private final static char DELIMITER_CHAR = '@';

    public static PacketInfo map(RTTData rttData) {
        if (rttData == null) {
            return EMPTY_PACKET;
        }
        byte[] data = rttData.getData();
        if (data == null) {
            return EMPTY_PACKET;
        }
        String dataString = new String(data);
        String[] infos = dataString.split(DELIMITER);
        if (infos.length != PAYLOAD_BLOCKS_AMOUNT) {
            return EMPTY_PACKET;
        }
        int number = Integer.valueOf(infos[0]);
        long pitcherSendTime = Long.valueOf(infos[1]);
        long catcherReceiveTime = Long.valueOf(infos[2]);
        return new PacketInfo(number, pitcherSendTime, catcherReceiveTime, rttData.getTime());
    }

    public static byte[] map(int packetNumber, int messageSize) {
        StringBuilder stringBuilder = new StringBuilder(messageSize);
        stringBuilder.append(packetNumber);
        stringBuilder.append(DELIMITER);
        stringBuilder.append(msOfMinute());
        stringBuilder.append(DELIMITER);
        int capacity = stringBuilder.length();
        for (char ch = 'A'; capacity < messageSize; capacity++, ch++) {
            stringBuilder.append(ch);
        }
        return stringBuilder.toString().getBytes();
    }

    public static void addCurrentTime(byte[] data) {
        int delimiterPosition = findSecondDelimiterPosition(data);
        insertTimeOfReceiving(data, delimiterPosition);
    }

    private void insertTimeOfReceiving(byte[] data, int delimiterPosition) {
        if (delimiterPosition > 0) {
            byte[] now = String.valueOf(msOfMinute()).concat(DELIMITER).getBytes();
            System.arraycopy(now, 0, data, delimiterPosition, now.length);
        }
    }

    private int findSecondDelimiterPosition(byte[] data) {
        int delimiterPosition = 0;
        for (int delimiterCount = 0; delimiterPosition < data.length && delimiterCount < 2; delimiterPosition++) {
            if (data[delimiterPosition] == DELIMITER_CHAR) {
                delimiterCount++;
            }
        }
        return delimiterPosition != data.length ? delimiterPosition : -1;
    }
}
