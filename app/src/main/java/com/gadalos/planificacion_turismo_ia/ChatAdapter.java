package com.yeltsin.tourapp.ui.chat;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yeltsin.tourapp.model.chat.Message;
import com.yeltsin.tourapp.model.chat.MessageRole;

import java.util.List;

import com.yeltsin.tourapp.R;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Message> messages;

    private static final int VIEW_TYPE_USER_MESSAGE = 0;
    private static final int VIEW_TYPE_SYSTEM_MESSAGE = 1;

    public ChatAdapter(List<Message> messages) {
        this.messages = messages;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getRole().equals(MessageRole.USER.getApiValue())) {
            return VIEW_TYPE_USER_MESSAGE;
        } else {
            return VIEW_TYPE_SYSTEM_MESSAGE;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_USER_MESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_user, parent, false);
            return new UserMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item_system, parent, false);
            return new SystemMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);

        if (holder.getItemViewType() == VIEW_TYPE_USER_MESSAGE) {
            ((UserMessageViewHolder) holder).bind(message);
        } else {
            ((SystemMessageViewHolder) holder).bind(message);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public void addMessage(Message message) {
        messages.add(message);

        notifyItemInserted(messages.size() - 1);
    }

    public void addLoadingMessage() {
        messages.add(new Message("Pensando, espera un momento...", MessageRole.SYSTEM));
        notifyItemInserted(messages.size() - 1);
    }

    public void removeLoadingMessage() {
        messages.remove(messages.size() - 1);
        notifyItemRemoved(messages.size());
    }

    static class UserMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        UserMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.userMessageText); // ID de tu TextView en message_item_user.xml
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
        }
    }

    static class SystemMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText;

        SystemMessageViewHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.systemMessageText); // ID de tu TextView en message_item_system.xml
        }

        void bind(Message message) {
            messageText.setText(message.getMessage());
        }
    }
}
