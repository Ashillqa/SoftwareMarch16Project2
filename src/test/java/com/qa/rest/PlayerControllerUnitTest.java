package com.qa.rest;

import com.qa.domain.Player;
import com.qa.dto.PlayerDTO;
import com.qa.service.playerService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class PlayerControllerUnitTest {
    @InjectMocks
    private PlayerController controller;
    @Mock
    private playerService service;

    private List<Player> players;
    private Player testPlayer;
    private Player testPlayerWithId;
    private Player newPlayer;
    private Player updatePlayer;
    private Long id = 1L;
    private PlayerDTO pDTO;
    private final ModelMapper mapper = new ModelMapper();
    private PlayerDTO mapToDto(Player player){return this.mapper.map(player,PlayerDTO.class);}

    @Before
    public void setUp(){
        this.players=new ArrayList<>();
        this.testPlayer= new Player("name",3L);
        this.players.add(testPlayer);
        this.testPlayerWithId = new Player(testPlayer.getName(),testPlayer.getGoals());
        this.testPlayerWithId.setId(this.id);
        this.pDTO = this.mapToDto(testPlayerWithId);
    }

    @Test
    public void getAllEmpsTest(){
        when(service.readPlayer()).thenReturn(this.players.stream().map(this::mapToDto).collect(Collectors.toList()));
        assertFalse("No emps found", this.controller.getAllPlayers().getBody().isEmpty());
        verify(service, times(1)).readPlayer();
    }

    @Test
    public void createNoteTest(){
        when(this.service.createPlayer(testPlayer)).thenReturn(this.pDTO);
        assertEquals(this.controller.createPlayer(testPlayer), new ResponseEntity<PlayerDTO>(this.pDTO, HttpStatus.CREATED));
        verify(this.service, times(1)).createPlayer(testPlayer);
    }

    @Test
    public void deleteNoteTestFalse(){
        this.controller.deletePlayer(id);
        verify(service, times(1)).deletePlayer(id);
    }

    @Test
    public void deleteEmpTestTrue(){
        when(service.deletePlayer(1L)).thenReturn(true);
        this.controller.deletePlayer(1L);
        verify(service, times(1)).deletePlayer(1L);
    }

    @Test
    public void getEmpByIDTest(){
        when(this.service.findPlayerById(id)).thenReturn(this.pDTO);
        assertEquals(this.controller.playerById(id), new ResponseEntity<PlayerDTO>(this.pDTO, HttpStatus.OK));
        verify(service, times(1)).findPlayerById(id);
    }


    @Test
    public void updateTest(){
        when(this.service.updatePlayer(id,updatePlayer)).thenReturn(pDTO);
        assertEquals(this.controller.updatePlayer(id,updatePlayer),new ResponseEntity<PlayerDTO>(this.pDTO,HttpStatus.OK));
    }

}