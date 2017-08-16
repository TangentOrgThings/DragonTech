// vim :set ts=2 sw=2 sts=2 expandtab smarttab :
/**
 *  WS-100+ Dragon Tech Industrial, Ltd.
 *
 *  Copyright 2017 Brian Aker <brian@tangent.org>, DarwinsDen.com
 *
 *  For device parameter information and images, questions or to provide feedback on this device handler,
 *  please visit:
 *
 *      github.com/TangentOrgThings/ws100plus/
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *	Author: Brian Aker <brian@tangent.org>
 *	Date: 2017
 *
 *	Changelog:
 *
 *
 *
 *
 */

def getDriverVersion () {
  return "4.57"
}

metadata {
  definition (name: "WS-100 Switch", namespace: "TangentOrgThings", author: "brian@tangent.org", ocfDeviceType: "oic.d.switch") {
    capability "Actuator"
    capability "Configuration"
    capability "Health Check"
    capability "Holdable Button"
    capability "Illuminance Measurement"
    capability "Indicator"
    capability "Light"
    capability "Polling"
    capability "Refresh"
    capability "Sensor"
    capability "Switch"

    command "tapUp2"
    command "tapDown2"
    command "tapUp3"
    command "tapDown3"
    command "holdUp"
    command "holdDown"

    attribute "Lifeline", "string"
    attribute "driverVersion", "string"
    attribute "FirmwareMdReport", "string"
    attribute "Manufacturer", "string"
    attribute "ManufacturerCode", "string"
    attribute "MSR", "string"
    attribute "ProduceTypeCode", "string"
    attribute "ProductCode", "string"
    attribute "WakeUp", "string"
    attribute "WirelessConfig", "string"

    // zw:L type:1001 mfr:000C prod:4447 model:3033 ver:5.14 zwv:4.05 lib:03 cc:5E,86,72,5A,85,59,73,25,27,70,2C,2B,5B,7A ccOut:5B role:05 ff:8700 ui:8700
    fingerprint type: "1001", mfr: "0184", prod: "4447", model: "3033", deviceJoinName: "WS-100" // cc: "5E, 86, 72, 5A, 85, 59, 73, 25, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B",
    fingerprint type: "1001", mfr: "000C", prod: "4447", model: "3033", deviceJoinName: "HS-WS100+" // cc: "5E, 86, 72, 5A, 85, 59, 73, 25, 27, 70, 2C, 2B, 5B, 7A", ccOut: "5B",
  }

  // simulator metadata
  simulator {
    status "on":  "command: 2003, payload: FF"
    status "off": "command: 2003, payload: 00"

    // reply messages
    reply "2001FF,delay 100,2502": "command: 2503, payload: FF"
    reply "200100,delay 100,2502": "command: 2503, payload: 00"
  }

  tiles(scale: 2) {
    multiAttributeTile(name:"switch", type: "lighting", width: 6, height: 4, canChangeIcon: true) {
      tileAttribute ("device.switch", key: "PRIMARY_CONTROL") {
        attributeState "on", label: '${name}', action: "switch.off", icon: "st.switches.switch.on", backgroundColor: "#00A0DC"
        attributeState "off", label: '${name}', action: "switch.on", icon: "st.switches.switch.off", backgroundColor: "#ffffff"
      }
      tileAttribute("device.status", key: "SECONDARY_CONTROL") {
        attributeState("default", label:'${currentValue}', unit:"")
      }
    }

    standardTile("tapUp2", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▲▲", backgroundColor: "#ffffff", action: "tapUp2", icon: "st.Home.home30"
    }

    standardTile("tapDown2", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▼▼", backgroundColor: "#ffffff", action: "tapDown2", icon: "st.Home.home30"
    }

    standardTile("tapUp3", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▲▲▲", backgroundColor: "#ffffff", action: "tapUp3", icon: "st.Home.home30"
    }

    standardTile("tapDown3", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Tap ▼▼▼", backgroundColor: "#ffffff", action: "tapDown3", icon: "st.Home.home30"
    }

    standardTile("holdUp", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Hold ▲", backgroundColor: "#ffffff", action: "holdUp", icon: "st.Home.home30"
    }

    standardTile("holdDown", "device.button", width: 2, height: 2, decoration: "flat") {
      state "default", label: "Hold ▼", backgroundColor: "#ffffff", action: "holdDown", icon: "st.Home.home30"
    }

    standardTile("indicator", "device.indicatorStatus", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "when off", action:"indicator.indicatorWhenOn", icon:"st.indicators.lit-when-off"
      state "when on", action:"indicator.indicatorNever", icon:"st.indicators.lit-when-on"
      state "never", action:"indicator.indicatorWhenOff", icon:"st.indicators.never-lit"
    }

    standardTile("refresh", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label:'', action:"refresh.refresh", icon:"st.secondary.refresh"
    }

    valueTile("firmwareVersion", "device.firmwareVersion", width:2, height: 2, decoration: "flat", inactiveLabel: false) {
      state "default", label: '${currentValue}'
    }

    valueTile("driverVersion", "device.driverVersion", width:2, height:2, inactiveLabel: true, decoration: "flat") {
      state "default", label: '${currentValue}'
    }

    standardTile("configure", "device.switch", width: 2, height: 2, inactiveLabel: false, decoration: "flat") {
      state "default", label:'', action:"configuration.configure", icon:"st.secondary.configure"
    }

    valueTile("driverVersion", "device.driverVersion", width:2, height:2, inactiveLabel: true, decoration: "flat") {
      state "default", label: '${currentValue}'
    }

    valueTile("illuminance", "device.illuminance", width: 2, height: 2) {
      state("illuminance", label:'${currentValue}', unit:"lux", backgroundColors:[
        [value: 8, color: "#767676"],
        [value: 300, color: "#ffa81e"],
        [value: 1000, color: "#fbd41b"]
      ])
    }

    main "switch"
    details(["switch", "tapUp2", "tapUp3", "holdUp", "tapDown2", "tapDown3", "holdDown", "indicator", "firmwareVersion", "driverVersion", "refresh", "configure", "illuminance"])
  }
}

def parse(String description) {
  def result = null

  log.debug "PARSE: ${description}"
  if (description.startsWith("Err")) {
    if (description.startsWith("Err 106")) {
      if (state.sec) {
        log.debug description
      } else {
        result = createEvent(
          descriptionText: "This device failed to complete the network security key exchange. If you are unable to control it via SmartThings, you must remove it from your network and add it again.",
          eventType: "ALERT",
          name: "secureInclusion",
          value: "failed",
          isStateChange: true,
        )
      }
    } else {
      result = createEvent(value: description, descriptionText: description, isStateChange: true)
    }
  } else if (description != "updated") {
    def cmd = zwave.parse(description)

    if (cmd) {
      result = zwaveEvent(cmd)

      if (!result) {
        log.warning "Parse Failed and returned ${result} for command ${cmd}"
        result = createEvent(value: description, descriptionText: description)
      } else {
        // If we displayed the result
        // log.debug "zwave.parse() debug: ${description}"
      }
    } else {
      log.info "zwave.parse() failed: ${description}"
      result = createEvent(value: description, descriptionText: description)
    }
  }

  return result
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicReport cmd) {
  log.debug "BasicReport()"
  def result = []

  result << createEvent(name: "switch", value: cmd.value ? "on" : "off", type:"physical")
  result << createEvent(name: "illuminance", value: cmd.value ? 300 : 8, unit: "lux")

  return result
}

def zwaveEvent(physicalgraph.zwave.commands.basicv1.BasicSet cmd) {
  log.debug "BasicSet()"
  def result = []

  result << createEvent(name: "switch", value: cmd.value ? "on" : "off", type: "physical")
  result << createEvent(name: "illuminance", value: cmd.value ? 300 : 8, unit: "lux")

  return result
}

def zwaveEvent(physicalgraph.zwave.commands.switchbinaryv1.SwitchBinaryReport cmd) {
  log.debug "SwitchBinaryReport()"

  def result = []

  result << createEvent(name: "switch", value: cmd.value ? "on" : "off", type: "digital")
  result << createEvent(name: "illuminance", value: cmd.value ? 300 : 8, unit: "lux")

  return result
}

def buttonEvent(button, held, buttonType) {
  log.debug("buttonEvent: $button  held: $held  type: $buttonType")
  button = button as Integer
  if (held) {
    createEvent(name: "button", value: "held", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true, type: "$buttonType")
  } else {
    createEvent(name: "button", value: "pushed", data: [buttonNumber: button], descriptionText: "$device.displayName button $button was pushed", isStateChange: true, type: "$buttonType")
  }
}

def zwaveEvent(physicalgraph.zwave.commands.sceneactuatorconfv1.SceneActuatorConfReport cmd) {
  log.debug("SceneActuatorConfReport: $cmd")

  if (cmd.sceneId == 1) {
    if (cmd.level != 255) {
      sendCommands([zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 1, level: 255, override: true)])
    }
  } else if (cmd.sceneId == 2) {
    if (cmd.level != 0) {
      sendCommands([zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 2, level: 0, override: true)])
    }
  }

  createEvent(descriptionText: "$device.displayName SceneActuatorConfReport: $cmd", displayed: true)
}

def zwaveEvent(physicalgraph.zwave.commands.sceneactivationv1.SceneActivationSet cmd) {
  log.debug("SceneActivationSet: $cmd")
  Integer button = ((cmd.sceneId + 1) / 2) as Integer
  Boolean held = !(cmd.sceneId % 2)
  buttonEvent(button, held, "digital")
}

def zwaveEvent(physicalgraph.zwave.commands.configurationv1.ConfigurationReport cmd) {
  def value = "when off"
  if (cmd.configurationValue[0] == 1) {value = "when on"}
  if (cmd.configurationValue[0] == 2) {value = "never"}
  createEvent([name: "indicatorStatus", value: value, display: false])
}

def zwaveEvent(physicalgraph.zwave.commands.manufacturerspecificv2.ManufacturerSpecificReport cmd) {
  log.debug("ManufacturerSpecificReport: $cmd");

  if ( cmd.manufacturerId == 0x000C ) {
    updateDataValue("manufacturer", "HomeSeer")
  } else if ( cmd.manufacturerId == 0x0184 ) {
    updateDataValue("manufacturer", "Dragon Tech Industrial, Ltd.")
  } else {
    if ( cmd.manufacturerId == 0x0000 ) {
      cmd.manufacturerId = 0x0184
    }

    updateDataValue("manufacturer", "Unknown Licensed Dragon Tech Industrial, Ltd.")
  }
  state.manufacturer= cmd.manufacturerName

  def manufacturerCode = String.format("%04X", cmd.manufacturerId)
  def productTypeCode = String.format("%04X", cmd.productTypeId)
  def productCode = String.format("%04X", cmd.productId)
  def wirelessConfig = "ZWP"

  sendEvent(name: "ManufacturerCode", value: manufacturerCode)
  sendEvent(name: "ProduceTypeCode", value: productTypeCode)
  sendEvent(name: "ProductCode", value: productCode)
  sendEvent(name: "WirelessConfig", value: wirelessConfig)

  def msr = String.format("%04X-%04X-%04X", cmd.manufacturerId, cmd.productTypeId, cmd.productId)
  updateDataValue("MSR", msr)

  sendEvent(name: "MSR", value: "$msr", descriptionText: "$device.displayName", isStateChange: false)
  [createEvent(name: "Manufacturer", value: "${cmd.manufacturerName}", descriptionText: "$device.displayName", isStateChange: false)]
}

def zwaveEvent(physicalgraph.zwave.commands.versionv1.VersionReport cmd) {
  def text = "$device.displayName: firmware version: ${cmd.applicationVersion}.${cmd.applicationSubVersion}, Z-Wave version: ${cmd.zWaveProtocolVersion}.${cmd.zWaveProtocolSubVersion}"
  state.firmwareVersion = cmd.applicationVersion+'.'+cmd.applicationSubVersion
  [createEvent(name: "firmwareVersion", value: "V ${state.firmwareVersion}", descriptionText: "$text", isStateChange: false)]
}

def zwaveEvent(physicalgraph.zwave.commands.firmwareupdatemdv2.FirmwareMdReport cmd) {
  def firmware_report = String.format("%s-%s-%s", cmd.checksum, cmd.firmwareId, cmd.manufacturerId)
  updateDataValue("FirmwareMdReport", firmware_report)
  [createEvent(name: "FirmwareMdReport", value: firmware_report, descriptionText: "$device.displayName FIRMWARE_REPORT: $firmware_report", isStateChange: false)]
}

def zwaveEvent(physicalgraph.zwave.Command cmd) {
  log.debug "ERROR: $cmd"
  [createEvent(descriptionText: "$device.displayName command not implemented: $cmd", displayed: true)]
}

def zwaveEvent(physicalgraph.zwave.commands.hailv1.Hail cmd) {
  [name: "hail", value: "hail", descriptionText: "Switch button was pressed", displayed: false]
}

def on() {
  log.debug "on()"
  sendEvent(buttonEvent(7, false, "digital"))
  setIlluminance(0xFF)
  delayBetween([
    zwave.basicV1.basicSet(value: 0xFF).format(),
    zwave.switchBinaryV1.switchBinaryGet().format()
  ])
}

def off() {
  log.debug "off()"
  sendEvent(buttonEvent(8, false, "digital"))
  setIlluminance(0x00)
  delayBetween([
    zwave.basicV1.basicSet(value: 0x00).format(),
    zwave.switchBinaryV1.switchBinaryGet().format()
  ])
}

def setIlluminance (level) {
  if (level > 0) {
    sendEvent(name: "illuminance", value: 300, unit: "lux")
  } else {
    sendEvent(name: "illuminance", value: 8, unit: "lux")
  }
}

def configure() {
  log.debug "configure()"
  switch (ledIndicator) {
    case "on":
    indicatorWhenOn()
    break
    case "off":
    indicatorWhenOff()
    break
    case "never":
    indicatorNever()
    break
    default:
    indicatorWhenOn()
    break
  }
}

/**
 * PING is used by Device-Watch in attempt to reach the Device
 * */
def ping() {
  refresh()
}

def refresh() {
  log.debug "refresh() called"
  delayBetween([
    zwave.switchBinaryV1.switchBinaryGet().format(),
    zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 1).format(),
    zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 2).format(),
    zwave.associationV2.associationGet(groupingIdentifier: 1).format(),
    zwave.manufacturerSpecificV1.manufacturerSpecificGet().format()
  ])
}

def poll() {
  delayBetween([
    zwave.switchBinaryV1.switchBinaryGet().format(),
    zwave.manufacturerSpecificV1.manufacturerSpecificGet().format()
  ])
}

void indicatorWhenOn() {
  sendEvent(name: "indicatorStatus", value: "when on", displayed: false)
  sendHubCommand(new physicalgraph.device.HubAction(zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 3, size: 1).format()))
}

void indicatorWhenOff() {
  sendEvent(name: "indicatorStatus", value: "when off", displayed: false)
  sendHubCommand(new physicalgraph.device.HubAction(zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 3, size: 1).format()))
}

void indicatorNever() {
  sendEvent(name: "indicatorStatus", value: "never", displayed: false)
  sendHubCommand(new physicalgraph.device.HubAction(zwave.configurationV1.configurationSet(configurationValue: [2], parameterNumber: 3, size: 1).format()))
}

def invertSwitch(invert=true) {
  if (invert) {
    sendCommands([
      zwave.configurationV1.configurationSet(configurationValue: [1], parameterNumber: 4, size: 1),
      zwave.configurationV1.configurationGet(parameterNumber: 4)
    ])
  }
  else {
    sendCommands([
      zwave.configurationV1.configurationSet(configurationValue: [0], parameterNumber: 4, size: 1),
      zwave.configurationV1.configurationGet(parameterNumber: 4)
    ])
  }
}

def zwaveEvent(physicalgraph.zwave.commands.deviceresetlocallyv1.DeviceResetLocallyNotification cmd) {
  state.reset = true
  [createEvent(descriptionText: cmd.toString(), isStateChange: true, displayed: true)]
}

def zwaveEvent(physicalgraph.zwave.commands.zwavecmdclassv1.NodeInfo cmd) {
  log.debug("NodeInfo: $cmd")
  [createEvent(name: "NIF", value: "$cmd", descriptionText: "$cmd")]
}

def zwaveEvent(physicalgraph.zwave.commands.centralscenev1.CentralSceneNotification cmd) {
  log.debug("sceneNumber: ${cmd.sceneNumber} keyAttributes: ${cmd.keyAttributes}")
  def result = []

  switch (cmd.sceneNumber) {
    case 1:
    // Up
    switch (cmd.keyAttributes) {
      case 1:
      result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      case 0:
      result << buttonEvent(7, false, "physical")
      setIlluminance(0xFF)
      sendEvent(name: "switch", value: "on", type: "physical")
      break
      break
      case 2:
      // Hold
      result << buttonEvent(1, true, "physical")
      setIlluminance(0xFF)
      sendEvent(name: "switch", value: "on", type: "physical")
      result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      break
      case 3:
      // 2 Times
      result << buttonEvent(2, false, "physical")
      break
      case 4:
      // 3 Three times
      result << buttonEvent(3, false, "physical")
      break
      default:
      log.debug ("unexpected up press keyAttribute: $cmd")
    }
    break

    case 2:
    // Down
    switch (cmd.keyAttributes) {
      case 1:
      result += sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      case 0:
      result << buttonEvent(8, false, "physical")
      setIlluminance(0x00)
      sendEvent(name: "switch", value: "off", type: "physical")
      break
      case 2:
      // Hold
      result << buttonEvent(4, false, "physical")
      setIlluminance(0x00)
      sendEvent(name: "switch", value: "off", type: "physical")
      result +=  sendHubCommand(new physicalgraph.device.HubAction(zwave.sceneActivationV1.sceneActivationSet(dimmingDuration: 0, sceneId: cmd.sceneNumber).format()))
      break
      case 3:
      // 2 Times
      result << buttonEvent(5, false, "physical")
      break
      case 4:
      // 3 Times
      result << buttonEvent(6, false, "physical")
      break
      default:
      log.debug ("unexpected down press keyAttribute: $cmd.keyAttributes")
    }
    break

    default:
    // unexpected case
    log.debug ("unexpected scene: $cmd.sceneNumber")
  }

  return result
}

def tapUp1Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "7"], descriptionText: "$device.displayName Tap-Up-1 (button 7) pressed", isStateChange: true, type: "$buttonType")
}

def tapDown1Response(String buttontype) {
  sendEvent(name: "status" , value: "Tap ▼")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "8"], descriptionText: "$device.displayName Tap-Down-1 (button 8) pressed", isStateChange: true, type: "$buttonType")
}

def tapUp2Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲▲")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "3"], descriptionText: "$device.displayName Tap-Up-2 (button 3) pressed", isStateChange: true, type: "$buttonType")
}

def tapDown2Response(String buttontype) {
  sendEvent(name: "status" , value: "Tap ▼▼")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "4"], descriptionText: "$device.displayName Tap-Down-2 (button 4) pressed", isStateChange: true, type: "$buttonType")
}

def tapUp3Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▲▲▲")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "5"], descriptionText: "$device.displayName Tap-Up-3 (button 5) pressed", isStateChange: true, type: "$buttonType")
}

def tapDown3Response(String buttonType) {
  sendEvent(name: "status" , value: "Tap ▼▼▼")
  sendEvent(name: "button", value: "pushed", data: [buttonNumber: "6"], descriptionText: "$device.displayName Tap-Down-3 (button 6) pressed", isStateChange: true, type: "$buttonType")
}

def holdUpResponse(String buttonType) {
  sendEvent(name: "status" , value: "Hold ▲")
  sendEvent(name: "button", value: "held", data: [buttonNumber: "7"], descriptionText: "$device.displayName Hold-Up (button 7) pressed", isStateChange: true, type: "$buttonType")
}

def holdDownResponse(String buttonType) {
  sendEvent(name: "status" , value: "Hold ▼")
  sendEvent(name: "button", value: "held", data: [buttonNumber: "8"], descriptionText: "$device.displayName Hold-Down (button 8) pressed", isStateChange: true, type: "$buttonType")
}

def tapUp2() {
  tapUp2Response("digital")
}

def tapDown2() {
  tapDown2Response("digital")
}

def tapUp3() {
  tapUp3Response("digital")
}

def tapDown3() {
  tapDown3Response("digital")
}

def holdUp() {
  holdUpResponse("digital")
}

def holdDown() {
  holdDownResponse("digital")
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationGroupingsReport cmd) {
  log.debug ("AssociationGroupingsReport: $cmd")

  state.groups = cmd.supportedGroupings

  if (cmd.supportedGroupings) {
    def cmds = []

    cmds << zwave.associationGrpInfoV1.associationGroupInfoGet(groupingIdentifier: 0x01, listMode: 0x01)
    cmds << zwave.associationGrpInfoV1.associationGroupNameGet(groupingIdentifier: 0x01)

    cmds << zwave.associationV2.associationSet(groupingIdentifier: 0x01, nodeId: [0x01])
    cmds << zwave.associationV2.associationSet(groupingIdentifier: 0x01, nodeId: [zwaveHubNodeId])
    cmds << zwave.associationV2.associationGet(groupingIdentifier: 0x01)

    if (cmd.supportedGroupings > 1) {
      for (def x = cmd.supportedGroupings +1; x <= cmd.supportedGroupings; x++) {
        cmds << zwave.associationGrpInfoV1.associationGroupInfoGet(groupingIdentifier: x, listMode: 0x01);
        cmds << zwave.associationGrpInfoV1.associationGroupNameGet(groupingIdentifier: x);
        cmds << zwave.associationV2.associationGet(groupingIdentifier: x);
      }
    }

    sendCommands(cmds, 1000)
  } else {
    [createEvent(descriptionText: "$device.displayName reported no groups", isStateChange: true, displayed: true)]
  }
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupInfoReport cmd) {
  log.debug ("AssociationGroupInfoReport() $cmd")
  [createEvent(descriptionText: "$device.displayName AssociationGroupInfoReport: $cmd", isStateChange: true, displayed: true)]
}

def zwaveEvent(physicalgraph.zwave.commands.associationgrpinfov1.AssociationGroupNameReport cmd) {
  log.debug "AssociationGroupNameReport() $cmd"
  [createEvent(descriptionText: "$device.displayName AssociationGroupNameReport: $cmd", displayed: true)]
}

def zwaveEvent(physicalgraph.zwave.commands.associationv2.AssociationReport cmd) {
  log.debug "$device.displayName AssociationReport()"

  // Lifeline
  if (cmd.groupingIdentifier == 0x01) {
    def string_of_assoc = ""
    cmd.nodeId.each {
      string_of_assoc += "${it}, "
    }
    def lengthMinus2 = string_of_assoc.length() - 2
    def final_string = string_of_assoc.getAt(0..lengthMinus2)

    if (cmd.nodeId.any { it == zwaveHubNodeId }) {
      Boolean isStateChange = state.isAssociated ?: false
      sendEvent(name: "Lifeline",
          value: "${final_string}",
          descriptionText: "${final_string}",
          displayed: true,
          isStateChange: isStateChange)

      state.isAssociated = true
    } else {
      Boolean isStateChange = state.isAssociated ? true : false
      sendEvent(name: "Lifeline",
          value: "",
          descriptionText: "${final_string}",
          displayed: true,
          isStateChange: isStateChange)
      state.isAssociated = false
    }
  } else {
    Boolean isStateChange = state.isAssociated ? true : false
    sendEvent(name: "Lifeline",
        value: "misconfigured",
        descriptionText: "misconfigured group ${cmd.groupingIdentifier}",
        displayed: true,
        isStateChange: isStateChange)
  }

  if (state.isAssociated == false) {
    sendCommands([zwave.associationV2.associationSet(groupingIdentifier: cmd.groupingIdentifier, nodeId: [zwaveHubNodeId])])
  } else {
    [createEvent(descriptionText: "$device.displayName is not associated", displayed: true)]
  }
}

def prepDevice() {
  [
    zwave.versionV1.versionGet(),
    zwave.associationV2.associationSet(groupingIdentifier: 0x01, nodeId: [zwaveHubNodeId]),
    zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 1, level: 255, override: true),
    zwave.sceneActuatorConfV1.sceneActuatorConfSet(sceneId: 2, level: 0, override: true),
    zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 1),
    zwave.sceneActuatorConfV1.sceneActuatorConfGet(sceneId: 2),
    zwave.manufacturerSpecificV2.manufacturerSpecificGet(),
    zwave.firmwareUpdateMdV1.firmwareMdGet(),
    zwave.associationV2.associationGet(groupingIdentifier: 0x01),
    zwave.associationV2.associationGroupingsGet(),
    zwave.switchBinaryV1.switchBinaryGet(),
    zwave.zwaveCmdClassV1.requestNodeInfo(),
  ]
}

def installed() {
  log.debug ("installed()")
  sendEvent(name: "numberOfButtons", value: 8, displayed: false)

  // Device-Watch simply pings if no device events received for 86220 (one day minus 3 minutes)
  sendEvent(name: "checkInterval", value: 86220, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])

  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)
  state.driverVersion = getDriverVersion()

  def cmds = prepDevice()
  cmds.each
  { zwaveCmd ->
    def hubCmd = []
    hubCmd << response(zwaveCmd)
    sendHubCommand(hubCmd, 1000)
  };

  configure()
}

def updated() {
  log.debug "updated()"

  // Device-Watch simply pings if no device events received for 86220 (one day minus 3 minutes)
  sendEvent(name: "checkInterval", value: 86220, displayed: false, data: [protocol: "zwave", hubHardwareId: device.hub.hardwareID])

  // Check in case the device has been changed
  state.manufacturer = null
  updateDataValue("MSR", null)
  updateDataValue("manufacturer", null)
  sendEvent(name: "numberOfButtons", value: 8, displayed: false)

  sendEvent(name: "driverVersion", value: getDriverVersion(), descriptionText: getDriverVersion(), isStateChange: true, displayed: true)
  state.driverVersion = getDriverVersion()

  def cmds = prepDevice()
  cmds.each
  { zwaveCmd ->
    def hubCmd = []
    hubCmd << response(zwaveCmd)
    sendHubCommand(hubCmd, 1000)
  };
}

private command(physicalgraph.zwave.Command cmd) {
  if (state.sec) {
    zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd).format()
  } else {
    cmd.format()
  }
}

private commands(sent_commands, delay=200) {
  sendCommands(sent_commands, delay)
}

/*****************************************************************************************************************
 *  Private Helper Functions:
 *****************************************************************************************************************/

/**
 *  encapCommand(cmd)
 *
 *  Applies security or CRC16 encapsulation to a command as needed.
 *  Returns a physicalgraph.zwave.Command.
 **/
private encapCommand(physicalgraph.zwave.Command cmd) {
  if (state.sec) {
    return zwave.securityV1.securityMessageEncapsulation().encapsulate(cmd)
  }
  else if (state.useCrc16) {
    return zwave.crc16EncapV1.crc16Encap().encapsulate(cmd)
  }
  else {
    return cmd
  }
}

/**
 *  prepCommands(cmds, delay=200)
 *
 *  Converts a list of commands (and delays) into a HubMultiAction object, suitable for returning via parse().
 *  Uses encapCommand() to apply security or CRC16 encapsulation as needed.
 **/
private prepCommands(cmds, delay=200) {
  return response(delayBetween(cmds.collect{ (it instanceof physicalgraph.zwave.Command ) ? encapCommand(it).format() : it }, delay))
}

/**
 *  sendCommands(cmds, delay=200)
 *
 *  Sends a list of commands directly to the device using sendHubCommand.
 *  Uses encapCommand() to apply security or CRC16 encapsulation as needed.
 **/
private sendCommands(cmds, delay=200) {
  sendHubCommand( cmds.collect{ (it instanceof physicalgraph.zwave.Command ) ? response(encapCommand(it)) : response(it) }, delay)
}
