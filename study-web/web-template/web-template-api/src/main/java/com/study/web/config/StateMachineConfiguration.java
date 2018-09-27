package com.study.web.config;

import java.util.EnumSet;

import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.EnumStateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import com.study.web.state.Events;
import com.study.web.state.States;

@EnableStateMachine
@Configuration
public class StateMachineConfiguration extends EnumStateMachineConfigurerAdapter<States, Events> {

	@Override
	public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
		states.withStates() //
				.initial(States.UNPAID) //
				.states(EnumSet.allOf(States.class)); //
	}

	@Override
	public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
		transitions.withExternal()//
				.source(States.UNPAID).target(States.WAITING_FOR_RECEIVE) //
				.event(Events.PAY) //
				.and() //
				.withExternal() //
				.source(States.WAITING_FOR_RECEIVE).target(States.DONE) //
				.event(Events.RECEIVE); //
	}

}
