package com.example.demo.schedule;

import com.example.demo.enums.ScrimStatus;
import com.example.demo.scrim.entity.Scrim;
import com.example.demo.scrim.repository.ScrimRepository;
import com.example.demo.scrim.service.ScrimService;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class ScrimSchedulerService {

    private final TaskScheduler taskScheduler;
    private final ScrimRepository scrimRepository;
    private final ScrimService scrimService;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    public ScrimSchedulerService(TaskScheduler taskScheduler,
            ScrimRepository scrimRepository,
            @Lazy ScrimService scrimService) {
        this.taskScheduler = taskScheduler;
        this.scrimRepository = scrimRepository;
        this.scrimService = scrimService;
    }

    public void scheduleScrim(Long scrimId) {
        Scrim scrim = scrimRepository.findById(scrimId)
                .orElseThrow(() -> new RuntimeException("Scrim not found"));

        Instant startTime = scrim.getScheduledTime()
                .atZone(ZoneId.systemDefault())
                .toInstant();

        ScheduledFuture<?> task = taskScheduler.schedule(
                () -> executeScrimTask(scrimId),
                startTime);

        scheduledTasks.put(scrimId, task);
        System.out.println("Tarea programada para scrim " + scrimId + " a las: " + scrim.getScheduledTime());
    }

    public void cancelScheduledTask(Long scrimId) {
        ScheduledFuture<?> task = scheduledTasks.remove(scrimId);
        if (task != null && !task.isDone()) {
            task.cancel(false);
            System.out.println("Tarea del scrim " + scrimId + " cancelada");
        }
    }

    @Transactional
    protected void executeScrimTask(Long scrimId) {
        System.out.println("EJECUTANDO TAREA DEL SCRIM " + scrimId);

        try {
            Scrim scrim = scrimRepository.findById(scrimId).orElse(null);

            if (scrim == null) {
                System.err.println("Scrim " + scrimId + " no encontrado");
                return;
            }

            ScrimStatus currentStatus = scrim.getStatus();

            if (currentStatus == ScrimStatus.CANCELLED ||
                    currentStatus == ScrimStatus.FINISHED ||
                    currentStatus == ScrimStatus.INGAME) {
                return;
            }
            if (currentStatus == ScrimStatus.CONFIRMED) {
                scrimService.internalStartScrim(scrimId);
            } else {
                scrimService.internalCancelScrim(scrimId);
            }

        } catch (Exception e) {
            System.err.println("Error en tarea del scrim " + scrimId + ": " + e.getMessage());
            e.printStackTrace();
        } finally {
            scheduledTasks.remove(scrimId);
        }
    }
}