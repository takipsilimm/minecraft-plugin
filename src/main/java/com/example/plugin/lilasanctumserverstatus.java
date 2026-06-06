package com.sanctum.status;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SanctumServerStatus extends JavaPlugin {

    // 🔴 YOUR WEBHOOK URL HERE
    private final String webhookUrl = "https://discord.com/api/webhooks/1512735292476493894/403_9E3e-9ZvLPHk8dr3lwRY0gZr9zR7mnAbDlwuZttxuXtdFE2x73doieb8GVpG5mYZ";

    // SERVER INFO
    private final String serverIP = "lilasanctum.aternos.me:25930";
    private final String port = "25930";

    @Override
    public void onEnable() {
        sendEmbed(
                "🟢 server Online",
                "server started successfully",
                65280,
                "online"
        );
    }

    @Override
    public void onDisable() {
        sendEmbed(
                "🔴 server Offline",
                "server has stopped",
                16711680,
                "offline"
        );
    }

    private void sendEmbed(String title, String description, int color, String status) {
        try {
            URL url = new URL(webhookUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = """
            {
              "embeds": [
                {
                  "title": "%s",
                  "description": "%s",
                  "color": %d,
                  "fields": [
                    {
                      "name": "server ip",
                      "value": "```%s```",
                      "inline": false
                    },
                    {
                      "name": "port",
                      "value": "```%s```",
                      "inline": true
                    },
                    {
                      "name": "status",
                      "value": "%s",
                      "inline": true
                    }
                  ],
                  "footer": {
                    "text": "lila sanctum server system"
                  }
                }
              ]
            }
            """.formatted(title, description, color, serverIP, port, status);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes());
                os.flush();
            }

            conn.getInputStream().close();

        } catch (Exception e) {
            getLogger().warning("Webhook failed: " + e.getMessage());
        }
    }
}