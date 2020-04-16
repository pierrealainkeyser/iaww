import GameView from '@/views/GameView';
import LoginView from '@/views/LoginView';
import LobbyView from '@/views/LobbyView';



export default [{
    path: '/',
    redirect: '/lobby'
  },
  {
    path: '/game/:game',
    component: GameView,
    props: true
  },
  {
    path: '/login',
    component: LoginView
  },
  {
    path: '/lobby',
    component: LobbyView
  }
];
