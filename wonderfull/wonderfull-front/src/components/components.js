 import ActiveCardsFlex from '@/components/game/ActiveCardsFlex'
 import BuildedCards from '@/components/game/BuildedCards'
 import CardStats from '@/components/game/CardStats'
 import CardView from '@/components/game/CardView'
 import SimpleCardView from '@/components/game/SimpleCardView'
 import EmpireTableStats from '@/components/game/EmpireTableStats'
 import Produce from '@/components/game/Produce'
 import Scoring from '@/components/game/Scoring'
 import SingleToken from '@/components/game/SingleToken'
 import Token from '@/components/game/Token'
 import AvailableTokens from '@/components/game/AvailableTokens'
 import EventList from '@/components/game/EventList'
 import AffectationDialog from '@/components/game/AffectationDialog'
 import SupremacyDialog from '@/components/game/SupremacyDialog'
 import EmpireCards from '@/components/game/EmpireCards'


 import FadeText from '@/components/system/FadeText'
 import SystemDrawer from '@/components/system/SystemDrawer'


 export default {
   install(Vue) {
     Vue.component('ActiveCardsFlex', ActiveCardsFlex);
     Vue.component('BuildedCards', BuildedCards);
     Vue.component('CardStats', CardStats);
     Vue.component('CardView', CardView);
     Vue.component('SimpleCardView', SimpleCardView);
     Vue.component('EmpireTableStats', EmpireTableStats);
     Vue.component('EmpireCards', EmpireCards);
     Vue.component('Produce', Produce);
     Vue.component('Scoring', Scoring);
     Vue.component('SingleToken', SingleToken);
     Vue.component('Token', Token);
     Vue.component('AvailableTokens', AvailableTokens);
     Vue.component('EventList', EventList);
     Vue.component('AffectationDialog', AffectationDialog);
     Vue.component('SupremacyDialog', SupremacyDialog);
     Vue.component('fade-text', FadeText);
     Vue.component('system-drawer', SystemDrawer);
   }
 }
