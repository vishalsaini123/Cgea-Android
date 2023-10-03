package com.creditgaea.sample.credit.java.chat.ui.adapter.listeners;

import android.view.View;

import com.quickblox.chat.model.QBChatMessage;

public interface MessageLongClickListener {
    void onMessageLongClicked (int itemViewType, View view, QBChatMessage qbChatMessage);
}
