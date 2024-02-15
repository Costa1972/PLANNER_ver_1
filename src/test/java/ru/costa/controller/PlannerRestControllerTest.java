package ru.costa.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;
import ru.costa.entity.Task;
import ru.costa.error.ErrorsPresentation;
import ru.costa.repository.NewTaskPayload;
import ru.costa.repository.TaskRepository;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlannerRestControllerTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    PlannerRestController plannerRestController;

    @Test
    @DisplayName("GET api/tasks возвращает HTTP-статус 200 OK и список задач")
    void handleGetAllTasks_ReturnsValidResponseEntity() {
        //given
        var tasks = List.of(new Task(UUID.randomUUID(), "Первая задача", false)
        , new Task(UUID.randomUUID(), "Вторая задача", false));
        doReturn(tasks).when(this.taskRepository).getAllTasks();
        //when
        var responseEntity = this.plannerRestController.handleGetAllTasks();
        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(tasks, responseEntity.getBody());
    }

    @Test
    void handleCreateNewTas_PayloadIsValid_ReturnsValidResponseEntity() {
        //given
        var details = "Третья задача";
        //when
        var responseEntity = this.plannerRestController.handleCreateNewTask(new NewTaskPayload(details)
        , UriComponentsBuilder.fromUriString("http://localhost:8080")
        , Locale.ENGLISH);
        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        if (responseEntity.getBody() instanceof Task task) {

            assertNotNull(task.uuid());
            assertEquals(details, task.details());
            assertFalse(task.completed());

            assertEquals(URI.create("http://localhost:8080/api/tasks/" + task.uuid()),
                    responseEntity.getHeaders().getLocation());

            verify(this.taskRepository).save(task);
        } else {
            assertInstanceOf(Task.class, responseEntity.getBody());
        }
        verifyNoMoreInteractions(this.taskRepository);
    }
    @Test
    void handleCreateNewTas_PayloadIsInvalid_ReturnsValidResponseEntity() {
        //given
        var details = "   ";
        var locale = Locale.US;
        var errorMessage = "Details is empty";

        doReturn(errorMessage).when(this.messageSource)
                .getMessage("task.create.details.error.not_set", new Object[0], locale);
        //when
        var responseEntity = this.plannerRestController.handleCreateNewTask(new NewTaskPayload(details),
                UriComponentsBuilder.fromUriString("http://lopcalhost:8080"), locale);
        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntity.getHeaders().getContentType());
        assertEquals(new ErrorsPresentation(List.of(errorMessage)), responseEntity.getBody());

        verifyNoInteractions(taskRepository);
    }

}